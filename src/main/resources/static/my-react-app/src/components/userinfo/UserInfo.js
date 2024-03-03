import React from 'react';

const UserInfo = (props) => {
    const { user_id, username, created, expiry_date, account_status, last_login, profile } = props;

    const containerInfo = {
        marginLeft: "450px",
        marginRight: "450px",
    }

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
            </div>
        </div>
  );
}

export default UserInfo;