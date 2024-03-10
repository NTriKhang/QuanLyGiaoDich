import React, { useState, useEffect } from 'react';

const UserInfo = (props) => {
	const { user_id, username, created, expiry_date, account_status, last_login, profile } = props;
	const [tablespace, setTablespace] = useState('');
	const [tablespaces, setTablespaces] = useState([]);
	const [dataQuota, setDataQuota] = useState('');
	const [maxQuotaSize, setMaxQuotaSize] = useState(null);

	const containerInfo = {
		marginLeft: "450px",
		marginRight: "450px",
	}
	const handleDataQuotaChange = (event) => {
		setDataQuota(event.target.value);
	}
	const handleTablespaceChange = (event) => {
		setTablespace(event.target.value);
	}
	// ... phần khác của component giữ nguyên

	useEffect(() => {
		const loadTablespacesAndQuota = async () => {
			try {
				const responseTablespaces = await fetch('http://localhost:8080/api/v1/users/tablespaces');
				const tablespacesData = await responseTablespaces.json();
				if (!responseTablespaces.ok) {
					throw new Error(`HTTP error! status: ${responseTablespaces.status}`);
				}
				setTablespaces(tablespacesData);

				// Cập nhật đường dẫn dựa trên controller của bạn
				if (tablespace) { // Chỉ fetch khi tablespace có giá trị
					const responseMaxQuota = await fetch(`http://localhost:8080/api/v1/users/maxQuota?tablespaceName=${encodeURIComponent(tablespace)}`);
					const maxQuotaData = await responseMaxQuota.json();
					if (!responseMaxQuota.ok) {
						throw new Error(`HTTP error! status: ${responseMaxQuota.status}`);
					}
					setMaxQuotaSize(maxQuotaData);
				}
			} catch (error) {
				console.error('Error loading resources:', error);
			}
		};

		loadTablespacesAndQuota();
	}, [tablespace]);

	const handleSubmit = async () => {
		try {
			const quotaValue = parseInt(dataQuota, 10);

			if (maxQuotaSize !== null && quotaValue > maxQuotaSize) {
				window.alert(`Quota không được lớn hơn kích thước tối đa của tablespace là ${maxQuotaSize} MB.`);
				return;
			}
			else {
				const response = await fetch('http://localhost:8080/api/v1/users/submitAccount', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({ username, tablespace, dataQuota: quotaValue }),
				});

				const responseData = await response.json();

				if (!response.ok) {
					window.alert(`Lỗi: ${responseData.error}`);
				} else {
					window.alert(`Thành công: ${responseData.message}`);
				}
			}
		} catch (error) {
			window.alert(`Lỗi: ${error.toString()}`);
		}
	};


	return (
		<div className='container-fluid'>
			<h2>Thông tin người dùng:</h2>
			<div style={containerInfo}>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>User_id:</span> {user_id}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Username:</span> {username}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Created:</span> {created}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Expiry_date:</span> {expiry_date}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Account_status:</span> {account_status}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Last_login:</span> {last_login}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Profile:</span> {profile}</p>
				<div className='text-start'>
					<span style={{ fontWeight: 'bold' }}>Tablespace:</span>
					<select value={tablespace} onChange={handleTablespaceChange}>
						{tablespaces.map((tablespaceName) => (
							<option key={tablespaceName} value={tablespaceName}>{tablespaceName}</option>
						))}
					</select>
				</div>
				<div className='text-start'>
					<label htmlFor="dataQuota" style={{ fontWeight: 'bold' }}>Data Quota (MB):</label>
					<input
						type="number"
						id="dataQuota"
						value={dataQuota}
						onChange={handleDataQuotaChange}
						style={{ width: '100%', padding: '5px' }}
					/>
				</div>
				<button onClick={handleSubmit}>Mở khóa tài khoản</button>
			</div>
		</div>
	);
}

export default UserInfo;