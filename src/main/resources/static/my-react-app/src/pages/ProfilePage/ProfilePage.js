import React, { useEffect, useState } from "react";

import Navbar from "../../components/navbar/Navbar";
import { MdOutlineAddToPhotos } from "react-icons/md";
import { Form, Modal, message } from 'antd';
import AddProfile from "./AddProfile";

const ProfilePage = () => {
  const [messageApi, contextHolder] = message.useMessage();
    const [open, setOpen] = useState(false);
    const [lisProfile, setListProfile] = useState([]);
    const [isAdded, setIsAdded] = useState(false);

    const showProfiles = () => {
        fetch('http://localhost:8080/api/v1/profile', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListProfile(data);
        });
    }

    const success = (p_content) => {
      messageApi.open({
        type: 'success',
        content: p_content,
      });
    };

    const groupByProfile = () => {
        const groupData = {};
        lisProfile.forEach(item => {
            if(!groupData[item.PROFILE]) {
                groupData[item.PROFILE] = [item];
            }
            else {
                groupData[item.PROFILE].push(item);
            }
        });
        return groupData;
    }

    const CollectionCreateForm = ({ initialValues, onFormInstanceReady }) => {
        const [form] = Form.useForm();
        useEffect(() => {
          onFormInstanceReady(form);
        }, []);
        return (
          <Form layout="vertical" form={form} name="form_in_modal">
            <AddProfile setOpen={setOpen} success={success} setIsAdded={setIsAdded} />
          </Form>
        );
      };
      const CollectionCreateFormModal = ({ open, onCancel, initialValues }) => {
        const [formInstance, setFormInstance] = useState();
        return (
          <Modal
            open={open}
            title="Add Profile"
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

    useEffect(() => {
      showProfiles();
    }, [])

    useEffect(() => {
      console.log("Successfully");
      showProfiles();
  }, [isAdded])

    return (
        <div>
          {contextHolder}
            <Navbar />
            <h1>Profiles Table</h1>
            <div style={{display: 'flex', flexDirection: 'column'}}>
                <button 
                    style={{width: "10%", marginLeft: 'auto', marginRight: '12px'}} 
                    className="btn btn-primary vertical-end"
                    onClick={() => {setOpen(true)}} >
                    Create
                    <MdOutlineAddToPhotos className="mx-1" />
                </button>
                <CollectionCreateFormModal
                    open={open}
                    onCancel={() => setOpen(false)}
                    initialValues={{
                    modifier: 'public',
                    }}
                />
                <table className="table">
                    <thead>
                        <tr className="table-active">
                            <th style={{border: "1px solid #fff"}}>PROFILE</th>
                            <th style={{border: "1px solid #fff"}}>RESOURCE_NAME</th>
                            <th style={{border: "1px solid #fff"}}>LIMIT</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.keys(groupByProfile()).map(profile => (
                            <React.Fragment key={profile}>
                                {groupByProfile()[profile].map((item, index) => (
                                    <tr key={`${profile}-${item}`}>
                                        {index === 0 ? <td className="text-center" rowSpan={16}>
                                            <p>{item.PROFILE}</p>
                                        </td> : null}
                                        <td>{item.RESOURCE_NAME}</td>
                                        <td>{item.LIMIT}</td>
                                    </tr>
                                ))}
                            </React.Fragment>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
};

export default ProfilePage;