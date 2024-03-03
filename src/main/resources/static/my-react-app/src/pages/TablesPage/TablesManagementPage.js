import React, { useState } from "react";

import Navbar from "../../components/navbar/Navbar";
import List from "../../components/listInfo/ListInfo";
import Button from "react-bootstrap/esm/Button";

const TablesManagementPage = () => {
    const [activeComponent, setActiveComponent] = useState(null);
    const [listInfo, setListInfo] = useState([]);

    const headerUserTable = [
        'USER_ID', 
        'FIRSTNAME', 
        'LASTNAME', 
        'ADDRESS', 
        'PHONE', 
        'EMAIL', 
        'USERNAME', 
        'PASSWORD', 
        'IMAGEPROFILE', 
        'MONEY', 
        'CREATED_DATE', 
        'LAST_LOGIN'
    ];

    const showTableUser = () => {
        setActiveComponent("Users");
        fetch('http://localhost:8080/api/v1/users/TableUser', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    return (
        <div>
            <div>
                <Navbar />
                <h2 className="m-4">Manage Tables Page</h2>
                <Button 
                    className="me-2"
                    onClick={() => showTableUser()} >
                    USERS
                </Button>
            </div>
            <div className="container mt-5 table-bordered">
                {activeComponent === 'Users' && <List listInfo={listInfo} headers={headerUserTable} />}
            </div>
        </div>
    );
};

export default TablesManagementPage;