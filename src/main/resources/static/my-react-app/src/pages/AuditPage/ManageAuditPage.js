import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";

import { Dropdown, Flex } from 'antd';
import { Button, Form, Input, Modal, Radio } from 'antd';

import AddAuditPage from "./AddAuditPage";

const ManageAuditPage = (props) => {
    const navigate = useNavigate();
    const dataFetchedRef = React.useRef(false);
    const [listInfo, setListInfo] = useState([]);
    const [formValues, setFormValues] = useState();
    const [open, setOpen] = useState(false);
    
    const onCreate = (values) => {
    console.log('Received values of form: ', values);
    setFormValues(values);
    setOpen(false);
  };

    const showListPolicy = () => {
        fetch('http://localhost:8080/api/v1/audit', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data[0])
            setListInfo(data);
        });
    }

    const deleteAudit = (objectName, policyName) => {
      try {
          const audit = {
              p_object_name: objectName,
              p_policy_name: policyName,
          }

          fetch('http://localhost:8080/api/v1/audit/deleteAudit', {
          method: 'POST',
          body: JSON.stringify(audit),
      })
      .then(res => {
          if(!res.ok) {
              throw new Error("Something went wrong!");
          }
          return res.json();
      })
      .then(res => {
          console.log("Success !");
      })
      }
      catch(err) {
          console.log("Error when fetching: " + err);
      }
  }
    
    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showListPolicy();
    }, []);

    useEffect(() => {
      showListPolicy();
    }, [listInfo])

    const items = [
        {
          key: '1',
          label: 'Create New',
        },
        {
          key: '2',
          label: 'Audit Trail',
        },
      ];

      const CollectionCreateForm = ({ initialValues, onFormInstanceReady }) => {
        const [form] = Form.useForm();
        useEffect(() => {
          onFormInstanceReady(form);
        }, []);
        return (
          <Form layout="vertical" form={form} name="form_in_modal">
            <AddAuditPage />
          </Form>
        );
      };
      const CollectionCreateFormModal = ({ open, onCreate, onCancel, initialValues }) => {
        const [formInstance, setFormInstance] = useState();
        return (
          <Modal
            open={open}
            title="Add Audit"
            okText="Create"
            cancelText="Cancel"
            okButtonProps={{
              autoFocus: true,
            }}
            footer={[]}
            onCancel={onCancel}
            destroyOnClose
            onOk={async () => {
              try {
                const values = await formInstance?.validateFields();
                formInstance?.resetFields();
                onCreate(values);
              } catch (error) {
                console.log('Failed:', error);
              }
            }}
          >
            <CollectionCreateForm
              initialValues={initialValues}
              onFormInstanceReady={(instance) => {
                setFormInstance(instance);
              }}
            />
          </Modal>
        );
      };  

    return (
        <div>
          <Navbar />
          <h2 style={{marginTop: 20, fontWeight: "bold"}}>Manage Audit</h2>
          <div className="container">

          <Flex align="flex-start" gap="small" vertical>
            <Dropdown.Button
                menu={{
                    items,
                    onClick: (
                        (e) => {
                            if(e.key == '1') {
                                console.log("click 1");
                                setOpen(true);
                            }
                            else if(e.key == '2') {
                                navigate("/auditTrial");
                            }
                        }
                    )
                }}
            >
            Option
            </Dropdown.Button>
        </Flex>
        <CollectionCreateFormModal
            open={open}
            onCreate={onCreate}
            onCancel={() => setOpen(false)}
            initialValues={{
            modifier: 'public',
            }}
        />
          {/* <button 
            className="btn btn-primary"
            onClick={() => {
                navigate("/addAudit")
            }} >Create new</button>

            <button 
                className="btn btn-primary mx-5"
                onClick={() => {
                    navigate("/auditTrial")
                }} >AuditTrail
            </button> */}

          <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/addAudit")
            }} >Create new</button>
             <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/auditManage")
            }} >Show audit</button>
            
             <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/auditWarning")
            }} >Show warning</button>
            
            

          <table>
                  <thead>
                      <tr>
                          <th>OBJECT_SCHEMA</th>
                          <th>OBJECT_NAME</th>
                          <th>POLICY_OWNER</th>
                          <th>POLICY_NAME</th>
                          <th>ENABLED</th>
                          <th>ACTION</th>
                      </tr>
                  </thead>
                  <tbody>
                      {listInfo.map((policy, index) => (
                          <tr key={index}>
                              <td className="text-center">{policy.OBJECT_SCHEMA}</td>
                              <td className="text-center">{policy.OBJECT_NAME}</td>
                              <td className="text-center">{policy.POLICY_OWNER}</td>
                              <td className="text-center">{policy.POLICY_NAME}</td>
                              <td className="text-center">{policy.ENABLED}</td>
                              <td className="text-center">
                                <button 
                                  className="btn btn-danger"
                                  onClick={() => deleteAudit(policy.OBJECT_NAME, policy.POLICY_NAME)} >
                                    DELETE
                                </button>
                              </td>
                          </tr>
                      ))}
                  </tbody>
              </table>
          </div>
        </div>  
      );
}


export default ManageAuditPage;