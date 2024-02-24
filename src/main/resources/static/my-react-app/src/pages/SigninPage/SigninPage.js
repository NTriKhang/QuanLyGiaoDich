import React, { useState } from "react";
import { json, useNavigate } from "react-router-dom";

import "./SigninPage.css";
import '@fortawesome/fontawesome-free/css/all.min.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faLock } from '@fortawesome/free-solid-svg-icons';
import Banner from '../../components/banner/Banner.js';

const Signin = (props) => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [usernameError, setUsernameError] = useState("")
    const [passwordError, setPasswordError] = useState("")
    
    const navigate = useNavigate();
        
    const onSigninClick = () => {

    }
    const onSignupClick = () => {
        navigate("/signup")
    }
    const onSubmit = async (event) => {
        event.preventDefault();
        var userData = {
            userName: username,
            password: password
        }
        console.log(userData)
        try{
            fetch('http://localhost:8080/api/v1/users/login',{
                method: 'POST',
                // mode: 'no-cors',
                // headers:{
                //     "Content-type": "multipart/form-data",
                // },
                body: JSON.stringify(userData)
            }).then(res => {
                console.log('submit successfully', res)
                localStorage.setItem('userNameKey', username)
                navigate("/")
            });
        }catch{
            console.log("err")
        }
        
    }
    return (
    <div className={"mainContainer row"}>
        <Banner />
        <div className={"col-6"}>
            <div className="form-container d-flex flex-column justify-content-end align-items-center">
                <div className={"titleContainer"}>
                    <div>Online Banking</div>
                </div>
                <br />
                <div className={"inputContainer w-100"}>
                    <div className="container-input-form">
                    <FontAwesomeIcon 
                        icon={faUser}
                        className="icon-input" />
                        <input
                            value={username}
                            placeholder="User name"
                            onChange={ev => setUsername(ev.target.value)}
                            className={"inputBox form-control pt-4 pb-4"} />
                    </div>
                    <label className="errorLabel">{usernameError}</label>
                </div>
                <br />
                <div className={"inputContainer w-100"}>
                    <div className="container-input-form">
                    <FontAwesomeIcon 
                        icon={faLock}
                        className="icon-input" />
                        <input
                            value={password}
                            placeholder="Password"
                            onChange={ev => setPassword(ev.target.value)}
                            className={"inputBox form-control pt-4 pb-4"}
                            type="password" />
                        <label className="errorLabel">{passwordError}</label>
                    </div>
                </div>
                <br />
                <div className={"inputContainer w-100"}>
                    <input
                        className={"inputButton w-100 m-0 mb-3"}
                        type="button"
                        onClick={onSubmit}
                        value={"Sign in"} />
                    <input
                        className={"inputButton w-100 m-0"}
                        type="button"
                        onClick={onSignupClick}
                        value={"Sign up"} />
                </div>
            </div>
        </div>
    </div>
    )
}

export default Signin