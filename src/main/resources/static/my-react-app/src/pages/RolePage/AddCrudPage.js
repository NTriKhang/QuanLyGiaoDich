import React, { useState } from 'react';
import { Button, Dropdown, Modal, Select, Space, Table, message } from 'antd';

const AddCrudPage = ({ onClose, isModalOpen, roleName }) => {

  const dataFetchedRef = React.useRef(false);
  const [selectedTable, setSelectedTable] = useState(null);
  const [listTable, setListTable] = useState(null);
  const [selectedPrivilege, setSelectedPrivilege] = useState(null);

  const onClick = (value) => {
    console.log(listTable[value].label)
    setSelectedTable(listTable[value].label)
  };
  const onClick_privilege = (value) => {
    setSelectedPrivilege(privliege[value].label)
    console.log(privliege[value].label)
  };

  const privliege = [
    {
      label: 'SELECT',
      value: 0,
    },
    {
      label: 'CREATE',
      value: 1,
    },
    {
      label: 'UPDATE',
      value: 2,
    },
    {
      label: 'DELETE',
      value: 3,
    },
  ];
  const getTablename = () => {
    fetch('http://localhost:8080/api/v1/audit/getAllTable', {
      method: 'GET'
    })
      .then(response => response.json())
      .then(data => {
        const modifiedData = data.map((element, index) => {
          return { label: element, value: index };
        });
        setListTable(modifiedData)

      });
  }
  const handleOk = () => {
    try {
      const assignRole = {
        stateMent: selectedPrivilege,
        tableName: selectedTable,
        roleName: roleName
      }
      fetch('http://localhost:8080/api/v1/privilege/grant_crud_role', {
        method: 'POST',
        body: JSON.stringify(assignRole)
      }).then(res => {
        console.log(res)
        if (res.ok) {
          message.info(selectedTable + ' ' + selectedPrivilege)
          onClose()
        }
      });
    } catch {
      console.log("err")
    }
  }
  React.useEffect(() => {
    if (dataFetchedRef.current) return;
    dataFetchedRef.current = true;

    getTablename()
  }, []);
  return (
    <>
      {
        <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={onClose}>
          <div>
            {listTable &&
              <Select
                showSearch
                style={{
                  width: 200,
                }}
                placeholder="Search to Select"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? '').includes(input)}
                filterSort={(optionA, optionB) =>
                  (optionA?.label ?? '').toLowerCase().localeCompare((optionB?.label ?? '').toLowerCase())
                }
                onChange={onClick}
                options={listTable}
              />
            }
          </div>
          <div className='mt-3'>
            {listTable &&
              <Select
                showSearch
                style={{
                  width: 200,
                }}
                placeholder="Search to Select"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? '').includes(input)}
                filterSort={(optionA, optionB) =>
                  (optionA?.label ?? '').toLowerCase().localeCompare((optionB?.label ?? '').toLowerCase())
                }
                onChange={onClick_privilege}
                options={privliege}
              />
            }
          </div>
        </Modal>
      }
    </>
  );
};
export default AddCrudPage;  