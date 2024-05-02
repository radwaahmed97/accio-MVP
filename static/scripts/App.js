import React from "react";
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import accioLogo from './acciologo.png'; // Fix: Change the import statement to use a valid variable name.
import './App.css';

function Homepage() {
  return (
    <div className="homepage">
      <html>
        <head>
          <meta charset="UTF-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <title>Accio</title>
        </head>
        <header>
          <h1 className="welcome">Welcome to Accio!</h1>
          <div className="booksearch">
            <input type="text" placeholder="Entre your book name.." />
            <button>Search</button>
          </div>
          <img src={accioLogo} className="App-logo" alt="logo" /> {/* Fix: Use the correct variable name */}
        </header>
        <body>
          <p>Accio is a book search engine that allows you to search for books by title, author, or genre.</p>
        </body>
        <footer>
          <p>Created by Team Accio</p>
        </footer>


      </html>
    </div>

  );
}

export default Homepage;