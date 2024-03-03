import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import Navbar from '../../components/navbar/Navbar';
import UserDetailAdminPage from "./UserDetailAdminPage";

const ManageUsersPage = () => {
    const [listInfo, setListInfo] = useState([]);
    const [detailUser, setDetailUser] = useState([]);
    const dataFetchedRef = React.useRef(false);

    const showListUser = () => {
        fetch('http://localhost:8080/api/v1/users/listUser', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }
    
    const showDetailUser = (user_id_p) => {
        try {
            const userInfo = {
                user_id: user_id_p,
            }

            fetch('http://localhost:8080/api/v1/users/findUserById', {
            method: 'POST',
            // headers:{
            //         "Content-type": "multipart/form-data",
            // },
            body: JSON.stringify(userInfo),
        })
        .then(res => {
            if(!res.ok) {
                throw new Error("Something went wrong!");
            }
            return res.json();
        })
        .then(res => {
            localStorage.setItem("DetailUser", JSON.stringify(res));
            navigate("/userDetailAdminPage");
        })
        }
        catch(err) {
            console.log("Error when fetching: " + err);
        }
    }

    React.useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showListUser();
    }, []);
    const navigate = useNavigate();
    return (
      <div>
        <Navbar />
        <div className="container">
        <table>
                <thead>
                    <tr>
                        <th>USER_ID</th>
                        <th>USERNAME</th>
                        <th>ACCOUNT_STATUS</th>
                        <th>PROFILE</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {listInfo.map((user, index) => (
                        <tr key={index}>
                            <td className="text-center">{user.USER_ID}</td>
                            <td className="text-center">{user.USERNAME}</td>
                            <td className="text-center">{user.ACCOUNT_STATUS}</td>
                            <td className="text-center">
                                <button className="btn btn-primary" onClick={() => showDetailUser(user.USER_ID)}>Info</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
      </div>  
    );
};

export default ManageUsersPage;