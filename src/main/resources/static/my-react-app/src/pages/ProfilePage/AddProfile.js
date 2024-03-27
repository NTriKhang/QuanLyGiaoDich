import React, { useEffect, useState } from "react";

import { 
  Form, 
  Input, 
  message,
} from 'antd';

const AddProfile = (props) => {
    const [messageApi, contextHolder] = message.useMessage();
    const [profileName, setProfileName] = useState("");
    const [isSuccess, setIsSuccess] = useState(false);
    const [sessionPerUser, setSessionPerUser] = useState(1);
    const [idleTime, setIdleTime] = useState(1);
    const [connectTime, setConnectTime] = useState(1);
    const [failedLoginAttempts, setFailLoginAttempts] = useState(1);
    const [passwordLockTime, setPasswordLockTime] = useState(1);

    const onHandleSubmit = () => {
        var str = profileName.trim();
        if(str.length != 0 && isSuccess) {
            props.setOpen(false);
        }
    }

    const error = (p_content) => {
      messageApi.open({
        type: 'error',
        content: p_content,
      });
    };

    const addProfile = (p_profileName, p_sessionPerUser, p_idleTime, p_connectTime, p_failed_login_attempts, p_password_lock_time) => {
      try {
        const profile = {
          profileName: p_profileName,
          sessionPerUser: p_sessionPerUser,
          idleTime: p_idleTime,
          connectTime: p_connectTime,
          failedLoginAttempts: p_failed_login_attempts,
          passwordLockTime: p_password_lock_time
      }
        fetch("http://localhost:8080/api/v1/profile/addProfile", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(profile),
        })
        .then(response => {
          return response.json()
        })
        .then(response => {
          if(response.success) {
            if(profileName) {
              props.setOpen(false);
              props.success(response.success);
              props.setIsAdded(true);
            }
            setIsSuccess(true);
          }
          else {
            if(profileName) {
              error(response.error);
            }
            setIsSuccess(false);
          }
        })
        .catch(err => console.log(err));
      }
      catch(err) {
        console.log(err);
      }
    }

    useEffect(() => {
    }, [])

    return (
        <div>
            <Form.Item
              name="title1"
              label="Profile Name"
              rules={[
                {
                  required: true,
                  message: 'Please input the profile name!',
                },
              ]}
            >
            <Input
                value={profileName}
                placeholder="Profile Name"
                onChange={ev => setProfileName(ev.target.value)}
                type="text" />
            </Form.Item>

            <Form.Item
              name="title2"
              label="Session Per User"
            >
            <Input
                value={profileName}
                placeholder="Session Per User"
                onChange={ev => setSessionPerUser(ev.target.value)}
                type="number" />
            </Form.Item>
            
            <Form.Item
              name="title3"
              label="Idle Time"
            >
            <Input
                value={profileName}
                placeholder="Idle Time"
                onChange={ev => setIdleTime(ev.target.value)}
                type="number" />
            </Form.Item>
            
            <Form.Item
              name="title4"
              label="Connect Time"
            >
            <Input
                value={profileName}
                placeholder="Connect Time"
                onChange={ev => setConnectTime(ev.target.value)}
                type="number" />
            </Form.Item>

            <Form.Item
              name="title5"
              label="Failed Login Attempts"
            >
            <Input
                value={profileName}
                placeholder="Failed Login Attempts"
                onChange={ev => setFailLoginAttempts(ev.target.value)}
                type="number" />
            </Form.Item>

            <Form.Item
              name="title6"
              label="Password Lock Time"
            >
            <Input
                value={profileName}
                placeholder="Password Lock Time"
                onChange={ev => setPasswordLockTime(ev.target.value)}
                type="number" />
            </Form.Item>
              
            {contextHolder}
            <button 
                className="btn btn-primary w-100"
                onClick={(e) => {
                    addProfile(profileName, sessionPerUser, idleTime, connectTime, failedLoginAttempts, passwordLockTime);
                    onHandleSubmit();
                }}    >
                    Add
            </button>
        </div>
    );
};

export default AddProfile;