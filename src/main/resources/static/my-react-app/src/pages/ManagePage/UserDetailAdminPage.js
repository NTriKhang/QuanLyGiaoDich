import React, { useEffect, useState } from "react";

import Navbar from "../../components/navbar/Navbar";
import UserInfo from "../../components/userinfo/UserInfo";


const UserDetailAdminPage = (props) => {
    const [detaiUser, setDetailUser] = useState([]);
    
    useEffect(() => {
        setDetailUser(JSON.parse(localStorage.getItem('DetailUser')));
    }, []);

    return (
        <div>
            <Navbar />
            <UserInfo 
                user_id={detaiUser.user_id} 
                username={detaiUser.username}
                created={detaiUser.created}
                expiry_date={detaiUser.expiry_date}
                account_status={detaiUser.account_status}
                last_login={detaiUser.last_login}
                profile={detaiUser.profile}
                tablespace={detaiUser.tablespace} />
        </div>
    )
};

const popup = {

}

export default UserDetailAdminPage;