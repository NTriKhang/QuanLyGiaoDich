import React, { useState } from 'react';

const UserInfo = (props) => {
    const { user_id, username, created, expiry_date, account_status, last_login, profile } = props;
	const [tablespace, setTablespace] = useState('');
	
	const containerInfo = {
        marginLeft: "450px",
        marginRight: "450px",
    }
    
    const handleTablespaceChange = (event) => {
        setTablespace(event.target.value);
    }
    
	const handleSubmit = async () => {
		try {
			const response = await fetch('http://localhost:8080/api/v1/users/submitAccount', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ username, tablespace }),
			});

			const data = await response.text();

			if (!response.ok) {
				throw new Error(data || `HTTP error! status: ${response.status}`); 
			}

			try {
				const result = JSON.parse(data);
				window.alert(`Thành công: ${result.message}`);
			} catch (e) {
				window.alert(`Thành công: ${data}`);
			}
		} catch (error) {
			console.error('Error:', error);
			window.alert(`Lỗi: ${error.message}`);
		}
	};

    return (
        <div className='container-fluid'>
        <h2>Thông tin người dùng:</h2>
            <div style={containerInfo}>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>User_id:</span> {user_id}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Username:</span> {username}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Created:</span> {created}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Expiry_date:</span> {expiry_date}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Account_status:</span> {account_status}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Last_login:</span> {last_login}</p>
                <p className='text-start'><span style={{fontWeight: 'bold'}}>Profile:</span> {profile}</p>
                 <div className='text-start'>
                    <span style={{fontWeight: 'bold'}}>Tablespace:</span>
                    <input type="text" value={tablespace} onChange={handleTablespaceChange} />
                </div>
                <button onClick={handleSubmit}>Mở khóa tài khoản</button>
            </div>
        </div>
  );
}

export default UserInfo;