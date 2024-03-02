import React, { useState, useEffect } from 'react';
import './TablespaceManagement.css';

const TablespaceManagement = () => {
	const [username, setUsername] = useState('');
	const [users, setUsers] = useState([]);
	const [tablespaces, setTablespaces] = useState([]);
	const dataFetchedRef = React.useRef(false);
	const [newTablespace, setNewTablespace] = useState({
		tablespaceName: '',
		datafilePath: '',
		size: ''
	});
	const [newDatafile, setNewDatafile] = useState({
		existingTablespaceName: '',
		newDatafilePath: '',
		newSize: ''
	});
	useEffect(() => {
		fetchUsers();
	}, []);

	const fetchUsers = () => {
		fetch('http://localhost:8080/api/v1/tableSpace/users', { method: 'GET' })
			.then(response => response.json())
			.then(data => {
				setUsers(data);
			})
			.catch(error => {
				console.error('Error fetching users:', error);
			});
	};
	const init = () => {
		fetch(`http://localhost:8080/api/v1/tableSpace`, {
			method: 'GET'
		})
			.then(response => response.json())
			.then(data => {
				setTablespaces(data);
			})
			.catch(error => {
				console.error('Error fetching tablespace data:', error);
			});
	}

	const searchTablespaces = () => {
		fetch(`http://localhost:8080/api/v1/tableSpace/${encodeURIComponent(username)}`)
			.then(response => {
				if (!response.ok) {
					throw new Error('Server error status: ' + response.status);
				}
				return response.json();
			})
			.then(data => {
				updateTablespacesList(data);
			})
			.catch(error => {
				console.error('Error fetching tablespace data:', error);
			});
	};
	const updateTablespacesList = (datafile) => {
		setTablespaces(datafile);
	};

	const addDatafileToTableList = (datafile) => {
		setTablespaces(prevTablespaces => [...prevTablespaces, datafile]);
	};

	const createTablespace = () => {
		const { tablespaceName, datafilePath, size } = newTablespace;
		const data = {
			tablespaceName,
			fileName: datafilePath,
			size: parseInt(size, 10)
		};

		fetch('http://localhost:8080/api/v1/tableSpace/create-tablespace', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(data)
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Server error status: ' + response.status);
				} else {
					window.alert('Tablespace đã được tạo thành công!');
					addDatafileToTableList(data);
					return response.json();
				}
			})
			.catch(error => {
				console.error('Error creating tablespace:', error);
			});
	};

	const addDatafileToTablespace = () => {
		const { existingTablespaceName, newDatafilePath, newSize } = newDatafile;
		const data = {
			tablespaceName: existingTablespaceName,
			fileName: newDatafilePath,
			size: parseInt(newSize, 10)
		};

		fetch('http://localhost:8080/api/v1/tableSpace/add-datafile-to-tablespace', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(data)
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Server error status: ' + response.status);
				} else {
					window.alert('Datafile đã được bổ sung thành công!!');
					addDatafileToTableList(data);
					return response.json();
				}
			})
			.catch(error => {
				console.error('Error adding datafile to tablespace:', error);
			});
	};
	const deleteTablespace = (tablespaceName, fileName) => {
		fetch(`http://localhost:8080/api/v1/tableSpace/delete?tablespaceName=${encodeURIComponent(tablespaceName)}&datafileName=${encodeURIComponent(fileName)}`, {
			method: 'GET',
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Server error status: ' + response.status);
				}
				return response.text();
			})
			.then(message => {
				window.alert(message);
				setTablespaces(prevTablespaces => prevTablespaces.filter(t => t.tablespaceName !== tablespaceName));
			})
			.catch(error => {
				console.error('Error managing tablespace and datafile:', error);
				window.alert('Lỗi khi quản lý tablespace và datafile: ' + error.message);
			});
	};




	React.useEffect(() => {
		if (dataFetchedRef.current) return;
		dataFetchedRef.current = true;
		fetchUsers();
		init();
	}, [])
	return (
		<div id="container" style={{ margin: '50px 10%' }}>
			<div className="form-group">
				<select
					id="username"
					value={username}
					onChange={e => setUsername(e.target.value)}
				>
					<option value="">Chọn người dùng</option>
					{Array.isArray(users) && users.map(user => (
						<option key={user} value={user}>{user}</option>
					))}
				</select>
				<button className="search-button" onClick={searchTablespaces}>Tìm kiếm</button>
			</div>

			<h1>Tablespace Information</h1>
			<table>
				<thead>
					<tr>
						<th>File Name</th>
						<th>Size (MB)</th>
						<th>Tablespace Name</th>
					</tr>
				</thead>
				<tbody>
					{tablespaces.map((tablespace, index) => (
						<tr key={index}>
							<td>{tablespace.fileName}</td>
							<td>{tablespace.size}</td>
							<td>{tablespace.tablespaceName}</td>
							<td>
								<button onClick={() => deleteTablespace(tablespace.tablespaceName, tablespace.fileName)}>Xóa</button>

							</td>
						</tr>
					))}
				</tbody>
			</table>
			<div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '20px' }}>
				<div id="create-tablespace-section" style={{ marginTop: '20px' }}>
					<h2>Tạo Tablespace mới</h2>
					<form>
						<div className="form-group">
							<label htmlFor="tablespaceNameInput">Tên Tablespace</label>
							<input
								type="text"
								id="tablespaceNameInput"
								placeholder="Tên Tablespace"
								value={newTablespace.tablespaceName}
								onChange={e => setNewTablespace({ ...newTablespace, tablespaceName: e.target.value })}
							/>
						</div>
						<div className="form-group">
							<label htmlFor="datafilePathInput">Đường dẫn Datafile</label>
							<input
								type="text"
								id="datafilePathInput"
								placeholder="Đường dẫn Datafile"
								value={newTablespace.datafilePath}
								onChange={e => setNewTablespace({ ...newTablespace, datafilePath: e.target.value })}
							/>
						</div>
						<div className="form-group">
							<label htmlFor="sizeInput">Kích thước (MB)</label>
							<input
								type="number"
								id="sizeInput"
								placeholder="Kích thước (MB)"
								value={newTablespace.size}
								onChange={e => setNewTablespace({ ...newTablespace, size: e.target.value })}
							/>
						</div>
						<button type="button" onClick={createTablespace}>Tạo Tablespace</button>
					</form>
				</div>
				<div id="add-datafile-section" style={{ marginTop: '20px' }}>
					<h2>Bổ sung Datafile vào Tablespace</h2>
					<form>
						<div className="form-group">
							<label htmlFor="existingTablespaceNameInput">Tên Tablespace hiện có</label>
							<input
								type="text"
								id="existingTablespaceNameInput"
								placeholder="Tên Tablespace hiện có"
								value={newDatafile.existingTablespaceName}
								onChange={e => setNewDatafile({ ...newDatafile, existingTablespaceName: e.target.value })}
							/>
						</div>
						<div className="form-group">
							<label htmlFor="newDatafilePathInput">Đường dẫn Datafile mới</label>
							<input
								type="text"
								id="newDatafilePathInput"
								placeholder="Đường dẫn Datafile mới"
								value={newDatafile.newDatafilePath}
								onChange={e => setNewDatafile({ ...newDatafile, newDatafilePath: e.target.value })}
							/>
						</div>
						<div className="form-group">
							<label htmlFor="newSizeInput">Kích thước mới (MB)</label>
							<input
								type="number"
								id="newSizeInput"
								placeholder="Kích thước mới (MB)"
								value={newDatafile.newSize}
								onChange={e => setNewDatafile({ ...newDatafile, newSize: e.target.value })}
							/>
						</div>
						<button type="button" onClick={addDatafileToTablespace}>Bổ sung datafile</button>
					</form>
				</div>
			</div>
		</div>
	);
}

export default TablespaceManagement;