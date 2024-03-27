import React, { useState } from 'react';
import { Button, Modal, Table } from 'antd';

const ListProcedureModal = ({ onClose, isModalOpen, roleName }) => {

  const dataFetchedRef = React.useRef(false);
  const [listUser, setListUser] = useState(null)
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [selectedRowData, setSelectedRowData] = useState(null);

  const columns = [
    {
      title: 'proc name',
      dataIndex: 'PROCNAME',
      key: 'PROCNAME',
    }
  ]
  const showRoleDetail = () => {
    fetch('http://localhost:8080/api/v1/privilege/getProc', {
      method: 'GET'
    })
      .then(response => response.json())
      .then(data => {
        const modifiedData = data.map((element, index) => {
          return { PROCNAME: element, key: index };
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
      const procName = selectedRowData.PROCNAME;
      console.log('Selected Username:', procName);

      try {
        const assignRole = {
          UserName: procName,
          RoleName: roleName
        }
        fetch('http://localhost:8080/api/v1/privilege/grant_execute_to_role', {
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
    console.log("test")
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
export default ListProcedureModal;  