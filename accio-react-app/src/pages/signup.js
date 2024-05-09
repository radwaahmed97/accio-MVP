
import '../signup.css';
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const Signup = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        phonenumber: '',
    });
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.username === '' || formData.email === '' || formData.password === '' || formData.phonenumber === '') {
            alert('All fields are mandatory');
            return;
        }
        console.log('Form Data', formData);
        setFormData({
            username: '',
            email: '',
            password: '',
            phonenumber: '',
        });

        try {
            const response = await fetch('http://localhost:8080/api/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
            console.log(response);
            if (!response.ok) {
                throw new Error('Sign up request failed');
            }
            setFormData({ username: '', email: '', password: '', phonenumber: ''});
            alert('Sign up successful!');
        } catch (error) {
            console.error('Error signing up:', error);
            alert('Sign up failed. Please try again.');
        }
    };

    return (
        <div className="signup">
            <div className="forms">
                <div className="form-content signup-form">
                    <form onSubmit={handleSubmit}>
                        <h2 className="signupform-title">Sign Up</h2>
                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <input className='userdata'
                                type="text"
                                id="username"
                                name="username"
                                value={formData.username}
                                onChange={handleInputChange}
                                placeholder="Enter your username"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input className='userdata'
                                type="email"
                                id="email"
                                name="email"
                                value={formData.email}
                                onChange={handleInputChange}
                                placeholder="Enter your email"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input className='userdata'
                                type="password"
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                placeholder="Enter your password"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="phonenumber">PhoneNumber</label>
                            <input className='userdata'
                                type="text"
                                id="phonenumber"
                                name="phonenumber"
                                value={formData.phonenumber}
                                onChange={handleInputChange}
                                placeholder="Enter your phonenumber"
                                required
                            />
                        </div>
                        <button className='signup-submit' type="submit">Sign Up</button>
                        <p className="link">Already have an account?<Link to={'/login'}><button className='login-from-signup'>Login</button></Link></p>
                    </form>
                </div>
            </div>
        </div>
    );
};
export default Signup;