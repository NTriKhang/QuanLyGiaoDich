import React, { useState } from "react";
import { json, useNavigate } from "react-router-dom";

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
                mode: 'no-cors',
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
    return <div className={"mainContainer"}>
        <div className={"titleContainer"}>
            <div>Login</div>
        </div>
        <br />
        <div className={"inputContainer"}>
            <input
                value={username}
                placeholder="User name"
                onChange={ev => setUsername(ev.target.value)}
                className={"inputBox"} />
            <label className="errorLabel">{usernameError}</label>
        </div>
        <br />
        <div className={"inputContainer"}>
            <input
                value={password}
                placeholder="Password"
                onChange={ev => setPassword(ev.target.value)}
                className={"inputBox"} />
            <label className="errorLabel">{passwordError}</label>
        </div>
        <br />
        <div className={"inputContainer"}>
            <input
                className={"inputButton"}
                type="button"
                onClick={onSubmit}
                value={"Sign in"} />
            <input
                className={"inputButton"}
                type="button"
                onClick={onSignupClick}
                value={"Sign up"} />
        </div>
    </div>
}

export default Signin