import "./Register.css";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
function Register() {
  const[userName,setUserName] = useState("");
  const[email,setEmail] = useState("");
  const[password,setPassword] = useState("");
  const navigate = useNavigate();
  const handleRegister = async (e) => {

  e.preventDefault();

  try {

      const response = await api.post(
          "/api/users/register",
          {
              username: userName,
              email: email,
              passwordHash: password
          }
      );
      console.log(response.data);

      alert("Registration Successful");

      navigate("/login");

  } catch (error) {

      console.error(error);

      alert("Registration Failed");
  }
};
  return (
    <div className="register-page">

      <div className="register-left">
        <h1>Torrent Application</h1>

        <h2>Decentralized File Sharing</h2>

        <p>
          Share files securely across peers.
          Fast, distributed and reliable.
        </p>
      </div>

      <div className="register-card">

        <h2>Create Account</h2>

        <p className="subtitle">
          Create your account to start sharing files
        </p>

        <form onSubmit={handleRegister}>

          <div className="input-group">
            <label>Username</label>
            <input
              type="text"
              placeholder="deepak"
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
            />
          </div>

          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              placeholder="deepak@example.com"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button
            type="submit"
            className="register-btn"
          >
            Create Account
          </button>

        </form>

        <p className="login-link">
          Already have an account?{" "}
          <Link to="/login">Login</Link>
        </p>

      </div>

    </div>
  );
}

export default Register;