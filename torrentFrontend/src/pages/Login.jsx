import "./Login.css";
import { useState } from "react";
import api from "../services/api";
import { useNavigate, Link } from "react-router-dom";


function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const handleLogin = async (e) => {
  e.preventDefault();
  console.log("Login button clicked");

    try {

        const response = await api.post("/api/users/login", {
        email: email,
        passwordHash: password
        });

        console.log(response.data);

        alert("Login Successful");
        localStorage.setItem("userId", response.data.userId);
        localStorage.setItem("username", response.data.userName);
        localStorage.setItem("token", response.data.token);
        console.log("Stored username:", localStorage.getItem("username"));
        console.log("Stored UserId",localStorage.getItem("userId"));
        console.log("Stored token",localStorage.getItem("token"));
        navigate("/");

    } catch (error) {

        console.error(error);

        alert("Invalid Credentials");
    }
    };
  return (
    <div className="login-page">

      <div className="login-left">
        <h1>Torrent Application</h1>

        <h2>Welcome Back</h2>

        <p>
          Login to continue sharing and downloading files
          across the torrent network.
        </p>
      </div>

      <div className="login-card">

        <h2>Login</h2>

        <p className="subtitle">
          Enter your credentials to continue
        </p>

        <form onSubmit={handleLogin}>

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
              onChange={(e)=>setPassword(e.target.value)}
            />
          </div>

          <button
            type="submit"
            className="login-btn"
          >
            Login
          </button>

        </form>

        <p className="register-link">
          Don't have an account?{" "}
          <Link to="/register">Register</Link>
        </p>

      </div>

    </div>
  );
}

export default Login;