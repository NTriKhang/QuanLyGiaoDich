import React, { useState } from 'react';
import { Button, Modal, Table } from 'antd';
const ListUserModal = ({ onClose, isModalOpen, roleName }) => {

  const dataFetchedRef = React.useRef(false);
  const [listUser, setListUser] = useState(null)
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [selectedRowData, setSelectedRowData] = useState(null);

  const columns = [
    {
      title: 'User name',
      dataIndex: 'USERNAME',
      key: 'USERNAME',
    }
  ]
  const showRoleDetail = () => {
    fetch('http://localhost:8080/api/v1/users/listUser', {
      method: 'GET'
    })
      .then(response => response.json())
      .then(data => {
        const modifiedData = data[0].map((element, index) => {
          return { USERNAME: element.USERNAME, key: index };
        });
        setListUser(modifiedData);
      });
  }
  const onSelectChange = (selectedRowKeys, selectedRows) => {
    setSelectedRowKeys(selectedRowKeys);
    setSelectedRowData(selectedRows && selectedRows.length > 0 ? selectedRows[0] : null);

    console.log(selectedRowData)
  };
  const handleOk = () => {
    if (selectedRowData) {
      const username = selectedRowData.USERNAME;
      console.log('Selected Username:', username);

      try {
        const assignRole = {
          UserName: username,
          RoleName: roleName
        }
        fetch('http://localhost:8080/api/v1/privilege/assignRoleToUser', {
          method: 'POST',
          body: JSON.stringify(assignRole)
        }).then(res => {
          console.log(res)
          if(res.ok){
            alert("assign successfully")
            onClose();

          }
        });
      } catch {
        console.log("err")
      }
    }
  };
  const rowClassName = (record, index) => {
    return selectedRowKeys.includes(index) ? 'selected-row' : '';
  };
  React.useEffect(() => {
    if (dataFetchedRef.current) return;
    dataFetchedRef.current = true;

    showRoleDetail();

  }, []);
  return (
    <>
      <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={onClose}>
        {listUser && <Table
          rowSelection={{
            selectedRowKeys,
            onChange: onSelectChange,
            type: 'radio',
          }}
          columns={columns}
          dataSource={listUser}
          rowClassName={rowClassName}
          onRow={(record, index) => {
            return {
              onClick: (event) => onSelectChange([index], [record]), // Set selected row on click
            };
          }}
        />
        }
      </Modal>
    </>
  );
};
export default ListUserModal;