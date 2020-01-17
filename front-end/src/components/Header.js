import React from 'react';
import {Link} from 'react-router-dom'
import './Header.css'
import camera from '../assets/camera.svg'

export default function Header() {
  return (
    <header id="main-header">
        <div className="header-content">
            <Link to="/">
              Pessoas
            </Link>
            <Link to="/verify">
              Reconhecer
            </Link>
              <Link to="/new">
                Novo Cadastro
              </Link>
        </div>
    </header>
  );
}
