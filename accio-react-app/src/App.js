import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homepage from './pages/homepage';
import Login from './pages/login';
import Signup from './pages/signup';
import ContactForm from './pages/contactus';
import MapComponent from './pages/nearplaces';
/**import Region from './pages/region';
import Language from './pages/language';
import Currency from './pages/currency';
import About from './pages/about';
import Privacy from './pages/privacy';
import Terms from './pages/terms';**/

function App() {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/contactus" element={<ContactForm />} />
        <Route path="/search" element={<MapComponent />} />
      </Routes>
    </Router>
  );
}

export default App;
