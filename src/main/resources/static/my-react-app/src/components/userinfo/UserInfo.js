import React, { useState, useEffect } from 'react';

import { Button, Select, Space, Tabs, Tag, message } from 'antd';
import TabPane from 'antd/es/tabs/TabPane';

import useGetTableName from '../../hooks/useGetTableName';
import useGrantPrevilegeTable from '../../hooks/useGrantPrevilegeTable';
import CollectionCreateFormModal from '../modal/CollectionCreateFormModal';

const UserInfo = (props) => {
	const { user_id, username, created, expiry_date, account_status, last_login, profile } = props;
	const [messageApi, contextHolder] = message.useMessage();
	const [open, setOpen] = useState(false);
	const [tablespace, setTablespace] = useState('');
	const [tablespaces, setTablespaces] = useState([]);
	const [dataQuota, setDataQuota] = useState('');
	const [maxQuotaSize, setMaxQuotaSize] = useState(null);
	const [listProfile, setListProfile] = useState([]);
	const [listProfileName, setListProfileName] = useState([]);
	const [profileValue, setProfileValue] = useState("");
	const [optionTables, setOptionTables] = useState([]);
	const [tablePrevilege, setTablePrevilege] = useState([]);
	const [listPrevilege, setListPrevilege] = useState([]);
	const [currentPrivilege, setCurrentPrivilege] = useState([]);

	const [tableName] = useGetTableName();

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

	const success = (p_content) => {
		messageApi.open({
		  type: 'success',
		  content: p_content,
		});
	  };

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

	const getProfiles = () => {
        fetch('http://localhost:8080/api/v1/profile', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListProfile(data);
        });
    }

	const changeProfileOfUser = (profileChange, usernameChange) => {
		try {
			const profileUser = {
				p_profile: profileChange,
				P_user_name: usernameChange
			}

			fetch('http://localhost:8080/api/v1/profile/alterProfile', {
				method: 'POST',
				body: JSON.stringify(profileUser)
			})
			.then(response => {
				if(!response.ok) {
					console.log("Error when fetching !!!");
				}
				return response.json();
			})
			.then(response => {
				console.log("Successfully !!!");
			})
		}
		catch(err) {
			console.log(err);
		}
	}

	const grantPrivilegeTable = (usernamePre, table, privilege) => {
		try {
			const grantPrivilege = {
				p_username: usernamePre,
				p_table: table,
                p_privilege:privilege
			}

			fetch('http://localhost:8080/api/v1/privilege/grantTable', {
				method: 'POST',
				body: JSON.stringify(grantPrivilege)
			})
			.then(response => {
				if(!response.ok) {
					console.log("Error when fetching !!!");
					return;
				}
				success("Grant Previlege successfully !");
				return response.json();
			})
		}
		catch(err) {
			console.log(err);
		}
	}


	const changeToOption = () => {
		var options = [];
		tableName.map((item, index) => {
			options.push({
				"value": item
			});
		})
		setOptionTables(options);
	}

	const getListProfileName = () => {
		var listName = [];
		var resArr = [];
		listProfile.map((item) => {
			if(item.PROFILE != listName[item.PROFILE]) {
				listName[item.PROFILE] = item.PROFILE;
			}
		});
		for(var key in listName) {
			resArr.push({
				value: key,
				label: key
			});
		}
		setListProfileName(resArr);
	};

	const convertPrevilegeToString = () => {
		var str = ""
		listPrevilege.map((item, index) => {
			if(index == (listPrevilege.length - 1)) {
				str = str + item;
			}
			else {
				str = str + (item + ",");
			}
		})
		return str;
	}

	const handlePrevilegeClick = () => {
		grantPrivilegeTable(username, tablePrevilege.toString(), convertPrevilegeToString());
	}

	const tagRender = (props) => {
	const { label, value, closable, onClose } = props;
	const onPreventMouseDown = (event) => {
		event.preventDefault();
		event.stopPropagation();
	};
	return (
		<Tag
			color={'cyan'}
			onMouseDown={onPreventMouseDown}
			closable={closable}
			onClose={onClose}
			style={{
				marginInlineEnd: 4,
			}}
			>
			{label}
		</Tag>
	);
	};

	useEffect(() => {
		changeToOption();
	}, [tableName]);

	useEffect(() => {
		getProfiles();
	}, []);
	
	useEffect(() => {
		getListProfileName();
		setProfileValue(profile);
	}, [listProfile]);

	return (
		<div className='container-fluid'>
			<CollectionCreateFormModal
				open={open}
				onCancel={() => setOpen(false)}
				initialValues={{
					modifier: 'public',
				}}
				type='RevokeObjectUser'
				titleModal="Revoke privilege"
				userPrivilege={username}
				tableSelect={currentPrivilege}
				setOpen={setOpen}
            />
			<h2>Thông tin người dùng:</h2>
			<div style={containerInfo}>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>User_id:</span> {user_id}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Username:</span> {username}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Created:</span> {created}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Expiry_date:</span> {expiry_date}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Account_status:</span> {account_status}</p>
				<p className='text-start'><span style={{ fontWeight: 'bold' }}>Last_login:</span> {last_login}</p>
				<div className='d-flex' style={{alignItems: 'center'}}>
					<p className='text-start'><span style={{ fontWeight: 'bold' }}>Profile:</span> 
						<Space wrap>
							<Select
								value={profileValue}
								style={{
									width: 160,
									marginLeft: '20px'
								}}
								onChange={(select) => {
									setProfileValue(select);
									changeProfileOfUser(select, username);
								}}
								options={listProfileName}
							/>
						</Space>
					</p>
					
				</div>
				<Tabs defaultActiveKey="1" centered>
					<TabPane tab="TableSpaces" key="1">
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
						<button
							style={{backgroundColor: '#64a5ff'}}
							className="btn mx-2 my-3 text-white"
							onClick={handleSubmit}>
								Mở khóa tài khoản
						</button>
					</TabPane>
					<TabPane tab="Grant Privilege" key="2">
						<div style={{marginBottom: '15px'}}>
							<label className='d-flex fw-bold'>Table: </label>
							<Select
								onChange={(e) => setTablePrevilege(e)}
								mode="multiple"
								tagRender={tagRender}
								maxCount={1}
								style={{
								width: '100%',
								}}
								options={optionTables}
							/>
						</div>
						<div>
							<label className='d-flex fw-bold'>Privilege: </label>
							<Select
								mode="multiple"
								style={{
									flex: 1,
									width: '100%'
								}}
								onChange={(e) => setListPrevilege(e)}
								options={[
									{
										value: 'SELECT',
									},
									{
										value: 'INSERT',
									},
									{
										value: 'UPDATE',
									},
									{
										value: 'DELETE',
									},
								]}
							/>
						</div>
						<div>
						{contextHolder}
							<button 
								className='btn text-white w-25 my-4' 
								style={{backgroundColor: '#64a5ff'}}
								onClick={() => {
									handlePrevilegeClick()
								}} >
									Save
							</button>
						</div>
					</TabPane>
					<TabPane tab="Revoke Privilege" key="3">
						{tableName.map((item) => (
							<Button
								className='mx-2'
								onClick={() => {
									setOpen(true)
									setCurrentPrivilege(item);
								}}
								key={item} >
									{item}
							</Button>
						))}
					</TabPane>
				</Tabs>
			</div>
		</div>
	);
}

export default UserInfo;