import React, { useState } from 'react';
import '../contactus.css';

const ContactForm = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        subject: '',
        message: ''
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formData.name === '' || formData.email === '' || formData.subject === '' || formData.message === '') {
            alert('Please fill in all fields');
            return;
        }
        console.log('Form Data:', formData);
        setFormData({
            name: '',
            email: '',
            subject: '',
            message: ''
        });
    };

    return (
        <body className='contactbody'>
            <div className="contact-container">
                <h2 className='Contactus'>Contact Us</h2>
                <form className='contactform' onSubmit={handleSubmit}>
                    <div className="contactform-group">
                        <label htmlFor="name">Name:</label>
                        <input className='contactuserdata'
                            type="text"
                            id="name"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className="contactform-group">
                        <label htmlFor="email">Email:</label>
                        <input className='contactuser'
                            type="contactemail"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className="contactform-group">
                        <label htmlFor="subject">Subject:</label>
                        <input className='contactuser'
                            type="contacttext"
                            id="subject"
                            name="subject"
                            value={formData.subject}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className="contactform-group">
                        <label htmlFor="message">Message:</label>
                        <textarea
                            id="message"
                            name="message"
                            rows="4"
                            value={formData.message}
                            onChange={handleInputChange}
                            required
                        ></textarea>
                    </div>
                    <button className="sendmessage" type="submit">Send Message</button>
                </form>
            </div>
        </body>
    );
};

export default ContactForm;
