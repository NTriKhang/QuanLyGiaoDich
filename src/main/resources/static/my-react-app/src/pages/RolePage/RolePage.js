import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";
import { Modal, Table } from "antd";
import ListUserModal from "./ListUser";
import ListProcedureModal from "./ListProcedureModal";

const RolePage = (props) => {
    const navigate = useNavigate();
    const dataFetchedRef = React.useRef(false);
    const [listInfo, setListInfo] = useState([]);

    const [selectedRole, setSelectedRole] = useState(null);

    const showListRole = () => {
        fetch('http://localhost:8080/api/v1/privilege', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data);
        });
    }
    const onDelete = (roleName, index) =>{
        fetch('http://localhost:8080/api/v1/privilege/' + roleName, {
            method: 'Delete'
        })
        .then(res => {
            console.log(res)
            if(res.status == 200){
                setListInfo(prevRole => {
                    const newRoles = [...prevRole];
                    newRoles.splice(index, 1);
                    return newRoles;
                });
                alert("role deleted successfully")
            }

        });
    }
    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showListRole();
    }, []);
    const handleDetailClick = (roleName) => {
        setSelectedRole(roleName);
    };

    const closeModal = () => {
        setSelectedRole(null);

    };
    return (
        <div>
          <Navbar />
          <div className="container">
          <button 
            className="btn btn-primary"
            onClick={() => {
                navigate("/addRole")
            }} >Create new</button>
          <table>
                  <thead>
                      <tr>
                          <th>ROLE</th>
                          <th>ORACLE MAINTAINED</th>
                          <th></th>
                          <th></th>
                      </tr>
                  </thead>
                  <tbody>
                      {listInfo.map((role, index) => (
                          <tr key={index}>
                              <td className="text-center">{role.role}</td>
                              <td className="text-center">{role.oracle_maintained}</td>
                              <td style={{ display: role.oracle_maintained === 'N' ? 'inline-block' : 'none' }}><button onClick={() => onDelete(role.role, index)} className="btn btn-danger">Delete</button></td>
                              <td style={{ display: 'inline-block' }}><button onClick={() => handleDetailClick(role.role)} className="btn btn-success">Detail</button></td>
                          </tr>
                      ))}
                  </tbody>
              </table>
          </div>
          {selectedRole && <RoleDetail roleName={selectedRole} onClose={closeModal} isModalOpen={true}/>}
        </div>  
      );
}
const RoleDetail = ({ roleName, onClose, isModalOpen}) => {
    const [addUser, setAddUser] = useState(null)
    const [addProc, setAddProc] = useState(null)
    const [roleDetail, setRoleDetail] = useState(null)
    const [listUser, setListUser] = useState(null)
    const dataFetchedRef = React.useRef(false);
    const columns = [
        {
          title: 'Table name',
          dataIndex: 'TableName',
          key: 'TableName',
        },
        {
          title: 'Privilege',
          dataIndex: 'Privilege',
          key: 'Privilege',
        }
    ]
    const columns_user = [
        {
          title: 'User name',
          dataIndex: 'Grantee',
          key: 'Grantee',
        }
    ]
    const showRoleDetail = () => {
        fetch('http://localhost:8080/api/v1/privilege/roleDetail/' + roleName, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            const modifiedData = data.map((element, index) => {
                // Add a key field to each element with the index value
                return { ...element, key: index };
            });
            setRoleDetail(modifiedData);
            console.log(modifiedData)
        });
    }
    const showUsers = () => {
        fetch('http://localhost:8080/api/v1/privilege/roleDetail_user/' + roleName, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data)
            const modifiedData = data.map((element, index) => {
                // Add a key field to each element with the index value
                return { ...element, key: index };
            });
            setListUser(modifiedData);
            console.log(modifiedData)
        });
    }
    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showRoleDetail();
        showUsers();
    }, []);
    const handleDetailClick = () => {
        setAddUser(true);
    };
    const handleDetailClick_proc = () => {
        setAddProc(true);
    };
    const closeModal = () => {
        setAddUser(null);
        showUsers()

    };
    const closeModal_proc = () => {
        setAddProc(null);
        showRoleDetail();

    };
    const handleRowClick_proc = (record, index) => {
        // Display an alert when a row is clicked
        //alert(`You clicked on row with index ${index}`);
        if(record.Privilege === 'EXECUTE'){
            let isconfirm = window.confirm(`Are you sure you want to remove ${record.Privilege} on ${record.TableName}?`);
            if(isconfirm){
                try {
                    const assignRole = {
                    UserName: record.TableName,
                    RoleName: roleName
                    }
                    fetch('http://localhost:8080/api/v1/privilege/revoke_execute_proc', {
                    method: 'POST',
                    body: JSON.stringify(assignRole)
                    }).then(res => {
                    console.log(res)
                    if(res.ok){
            
                        alert("revoke successfully")
                        onClose()
                    }
                    });
                } catch {
                    console.log("err")
                }
            }
        }
      };
      const handleRowClick_user = (record, index) => {
        console.log(record)
        // Display an alert when a row is clicked
        // alert(`You clicked on row with index ${index}`);
            let isconfirm = window.confirm(`Are you sure you want to revoke ${record.Grantee} on ${roleName}?`);
            if(isconfirm){
                try {
                    const assignRole = {
                    UserName: record.Grantee,
                    RoleName: roleName
                    }
                    fetch('http://localhost:8080/api/v1/privilege/revoke_role_from_user', {
                    method: 'POST',
                    body: JSON.stringify(assignRole)
                    }).then(res => {
                    console.log(res)
                    if(res.ok){
            
                        try{
                            fetch('http://localhost:8080/api/v1/users/logout_all/' + record.Grantee,{
                                method: 'GET',
                                  // mode: 'no-cors',
                            }).then(res => {
                                alert("revoke successfully")
                                onClose()
                            });
                        }catch{
                            console.log("err")
                        }
                    }
                    });
                } catch {
                    console.log("err")
                }
            }
      };
    return (
      <>
        <Modal title="Privilege Modal" open={isModalOpen} onOk={onClose} onCancel={onClose}>
            <div className="mb-3">
                <h5 >OBJECT priv</h5>
                <button className="btn btn-primary" onClick={handleDetailClick_proc}>add proc</button>
                {roleDetail && <Table columns={columns} dataSource={roleDetail} onRow={(record, index) => ({
                    onClick: () => handleRowClick_proc(record, index),
                })} />}
                {addProc && <ListProcedureModal onClose={closeModal_proc} isModalOpen={true} roleName={roleName}/>}
            </div>
            <div className="mb-3">
                <h5 className="mb-3">Assigned user</h5>
                <button className="btn btn-primary" onClick={handleDetailClick}>Add</button>
                {roleDetail && <Table columns={columns_user} dataSource={listUser} onRow={(record, index) => ({
                    onClick: () => handleRowClick_user(record, index),
                })}/>}
                {addUser && <ListUserModal onClose={closeModal} isModalOpen={true} roleName={roleName}/>}
            </div>
        </Modal>
      </>
    );
  };
export default RolePage;