import React, { useState } from "react";
import "./SignupPage.css"
import { useNavigate } from "react-router-dom";

const Signup = (props) => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [firstname, setFirstname] = useState("")
    const [lastname, setLastname] = useState("")
    const [phone, setPhone] = useState("")
    const [email, setEmail] = useState("")
    
    const navigate = useNavigate();
        
    const onNextClick = () => {
        
    }
    const onBackClick = () => {
        navigate('/signin')
    }
    return <div className={""}>
        <div className={"titleContainer"}>
            <div>Sign up</div>
        </div>
        <br />
        <div className="input_contain">
            <div className={"m-2"}>
                <input
                    value={email}
                    placeholder="User name"
                    onChange={ev => setUsername(ev.target.value)}
                    className={"inputBox"} />
            </div>
            <div className={"m-2"}>
                <input
                    value={password}
                    placeholder="Password"
                    onChange={ev => setPassword(ev.target.value)}
                    className={"inputBox"} />
            </div>
        </div>
        <div className="input_contain">
            <div className={"m-2"}>
                <input
                    value={email}
                    placeholder="First name"
                    onChange={ev => setFirstname(ev.target.value)}
                    className={"inputBox"} />
            </div>
            <div className={"m-2"}>
                <input
                    value={password}
                    placeholder="Last name"
                    onChange={ev => setLastname(ev.target.value)}
                    className={"inputBox"} />
            </div>
        </div>
        <div className="input_contain">
            <div className={"m-2"}>
                <input
                    value={email}
                    placeholder="Phone"
                    onChange={ev => setPhone(ev.target.value)}
                    className={"inputBox"} />
            </div>
            <div className={"m-2"}>
                <input
                    value={password}
                    placeholder="Email"
                    onChange={ev => setEmail(ev.target.value)}
                    className={"inputBox"} />
            </div>
        </div>
        <br />
            <div className={""}>
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={onNextClick}
                    value={"Next"} />
            </div>
            <div className={""}>
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={onBackClick}
                    value={"Back"} />
            </div>
    </div>
}

export default Signup