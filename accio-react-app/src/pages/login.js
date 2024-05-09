import { Link } from 'react-router-dom';
import '../login.css';
import React from 'react';


function Login() {

    const forms = document.querySelector(".forms"),
        pwShowHide = document.querySelectorAll(".eye-icon"),
        links = document.querySelectorAll(".link");

    pwShowHide.forEach(eyeIcon => {
        eyeIcon.addEventListener("click", () => {
            let pwFields = eyeIcon.parentElement.parentElement.querySelectorAll(".password");
            pwFields.forEach(password => {
                if (password.type === "password") {
                    password.type = "text";
                    eyeIcon.classList.replace("bx-hide", "bx-show");
                    return;
                }
                password.type = "password";
                eyeIcon.classList.replace("bx-show", "bx-hide");
            })
        })
    })

    links.forEach(link => {
        link.addEventListener("click", e => {
            e.preventDefault(); //preventing form submit
            forms.classList.toggle("show-signup");
        })
    })

    return (
        <div className="login">
            <div className="loginforms">
                <div className="loginform-content">
                    <form action="#">
                        <h2 className="loginform-title">Login</h2>
                        <div className="loginform-group">
                            <label htmlFor="email">Email</label>
                            <input className='loginuserdata' type="email" id="email" placeholder="Enter your email" required />
                        </div>
                        <div className="loginform-group">
                            <label htmlFor="password">Password</label>
                            <input className="loginuserdata" type="password" id="password"  placeholder="Enter your password" required />
                            <i className='bx bx-hide eye-icon'></i>
                        </div>
                        <button className='login-submit' type="submit">Login</button>
                        <p className="link">Don't have an account? <Link to="/signup"><button className='signup-from-login'>Sign up</button></Link></p>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;