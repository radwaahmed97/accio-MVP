import React from "react";
import treeofbooks from '../treeofbooks.jpg';
import { Link } from "react-router-dom";
import '../homepage.css';

function Homepage() {

    const listData = [
        { title: 'More', details: { detail1: 'Region', detail2: 'Language', detail3: 'Currency' } },
        { title: 'Help and support', details: { detail1: 'Contact Customer Service' } },
        { title: 'Settings and legal', details: { detail1: 'Privacy & cookies', detail2: 'About Accio.com', detail3: 'Terms & conditions' } }
    ]

    const listMapping = {
        'Region': './pages/region',
        'Language': './pages/language',
        'Currency': './pages/currency',
        'Contact Customer Service': '/contactus',
        'Privacy & cookies': './pages/privacy',
        'About Accio.com': './pages/about',
        'Terms & conditions': './pages/terms'
    };

    const [isVisible, setIsVisible] = React.useState(false);

    const toggleListVisibility = () => {
        setIsVisible(!isVisible);
    };

    return (
        <div className="homepage">
            <html>
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Accio</title>
                </head>
                <header>
                    <Link to="/login">
                        <button className="loginbutton">login</button>
                    </Link>
                    <Link to="/signup">
                        <button className="signupbutton">sign up</button>
                    </Link>
                    <button onClick={toggleListVisibility} className="listbutton">=</button>
                    {isVisible && (
                        <ul className="list">
                            {listData.map((item, index) => (
                                /**<button onClick={toggleListVisibility} className="itemdetails"></button>**/
                                <li key={index}>
                                    <div className="list-item">
                                        <h3>{item.title}</h3>
                                        <ul className="itemdeatils">
                                            {Object.values(item.details).map((detail, index) => (
                                                <Link to={listMapping[detail]}>
                                                    <li key={index}>{detail}</li>
                                                </Link>
                                            ))}
                                        </ul>
                                    </div>
                                </li>
                            ))};
                        </ul>
                    )}
                    <h1 className="welcome">Accio!</h1>
                    <div>
                        <input className="searchinput" type="text" placeholder="Entre your book name.." />
                        <Link to={'/search'} className="mapslink"><button className="searchbutton">Search</button></Link>
                    </div>
                    <img src={treeofbooks} className="App-logo" alt="logo" /> {/* Fix: Use the correct variable name */}
                </header>
                <footer>
                    <p>Created by Team Accio</p>
                </footer>


            </html>
        </div>

    );
}

export default Homepage;