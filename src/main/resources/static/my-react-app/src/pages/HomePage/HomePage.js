import React from "react"
import { useNavigate } from "react-router-dom";

import Navbar from "../../components/navbar/Navbar.js";

const Home = (props) => {
    const { loggedIn, email } = props
    const navigate = useNavigate();
    
    const onSignInClick = () => {
        // You'll update this function later
        if (loggedIn) {
            localStorage.removeItem("user")
            props.setLoggedIn(false)
        } else {
            navigate("/signin")
        }

    }
    const onSignOutClick = () => {
        // You'll update this function later
        if(localStorage.getItem('userNameKey') != null){
            try{
                console.log('trye')
                fetch('http://localhost:8080/api/v1/users/logout/' + localStorage.getItem('userNameKey'),{
                    method: 'GET',
                    mode: 'no-cors',
                }).then(res => {
                    console.log('submit successfully', res)
                    localStorage.removeItem("userNameKey")
                    alert('logout success')
                });
            }catch{
                console.log("err")
            }
        }

    }
    return (
       <div>
            <Navbar />
            <input
                    className={"inputButton"}
                    type="button"
                    onClick={onSignOutClick}
                    value={"Sign out"} />
            {/* <div className="mainContainer">
            <div className={"titleContainer"}>
                <div>Welcome!</div>
            </div>
            <div>
                This is the home page.
            </div>
            <div className={"buttonContainer"}>
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={onSignInClick}
                    value={"Sign in"} />
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={onSignOutClick}
                    value={"Sign out"} />
            </div>
        </div> */}
       </div>
    )
}

export default Home