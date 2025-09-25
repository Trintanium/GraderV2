import { useUser } from "@/contexts/UserContext";
import { Link, useLocation, useNavigate } from "react-router-dom";
import Anonymous from "@/assets/Anonymous.png";
import { useState, useEffect, useRef } from "react";

function Navbar() {
  const { user, setUser } = useUser();
  const [userInfo, setUserInfo] = useState(false);
  const [mobileMenu, setMobileMenu] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const mobileRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    setUser(null);
    setUserInfo(false);
    navigate("/auth");
    setMobileMenu(false);
  };

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        userInfo &&
        dropdownRef.current &&
        !dropdownRef.current.contains(e.target as Node) &&
        !(e.target as HTMLElement).closest("#profile-img")
      ) {
        setUserInfo(false);
      }
      if (
        mobileMenu &&
        mobileRef.current &&
        !mobileRef.current.contains(e.target as Node) &&
        !(e.target as HTMLElement).closest(".fa-bars")
      ) {
        setMobileMenu(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [userInfo, mobileMenu]);

  const navLinks = [
    { name: "Home", path: "/" },
    { name: "Tasks", path: "/tasks" },
    { name: "About", path: "/about" },
    { name: "Tags", path: "/tags" },
  ];

  return (
    <nav className="relative top-0 left-0 w-screen z-50 bg-[#112538] shadow-lg flex justify-between items-center h-16 px-6 sm:px-12 text-white">
      {/* Logo */}
      <div className="text-2xl font-bold tracking-wider hover:text-purple-300 transition">
        <Link to="/">Logo</Link>
      </div>

      {/* Desktop Links */}
      <div className="hidden sm:flex gap-6 items-center">
        {navLinks.map((link) => (
          <Link
            key={link.path}
            to={link.path}
            className={`px-8 py-2 rounded-md font-medium ease-in-out trasition-0.5 transition ${
              currentPath === link.path
                ? "bg-gradient-to-l from-[#03346E] to-[#17548B] text-white"
                : "hover:bg-[#17548B] hover:text-white"
            }`}
          >
            {link.name}
          </Link>
        ))}

        {user ? (
          <div ref={dropdownRef} className="relative">
            <img
              id="profile-img"
              onClick={() => setUserInfo((prev) => !prev)}
              src={user.profilePicture || Anonymous}
              alt="Profile"
              className="w-9 h-9 rounded-full cursor-pointer border-2 border-transparent hover:border-purple-400 transition"
            />

            <div
              className={`absolute right-0 mt-2 w-48 bg-[#9c3dcf] rounded-xl shadow-lg flex flex-col overflow-hidden transition-all duration-200 ${
                userInfo
                  ? "opacity-100 translate-y-0 visible"
                  : "opacity-0 -translate-y-2 invisible"
              }`}
            >
              <div className="p-4 border-b border-[#602780]">
                <div className="font-semibold">{user.username || "null"}</div>
                <div className="text-sm text-gray-200">
                  {user.email || "null"}
                </div>
              </div>
              <Link
                to="/settings"
                className="p-3 hover:bg-[#7a3dc0] transition"
              >
                User Settings
              </Link>
              <button
                onClick={handleLogout}
                className="text-left p-3 hover:bg-[#7a3dc0] transition"
              >
                Logout
              </button>
            </div>
          </div>
        ) : (
          <Link
            to="/auth"
            className="px-3 py-2 rounded-md bg-[#b588d5] font-medium hover:bg-[#a26ac2] transition"
          >
            Sign Up / Login
          </Link>
        )}
      </div>

      {/* Mobile menu icon */}
      <div className="sm:hidden">
        <i
          className="fa-solid fa-bars text-xl cursor-pointer hover:text-purple-300 transition"
          onClick={() => setMobileMenu((prev) => !prev)}
        ></i>
      </div>

      {/* Mobile Menu */}
      <div
        ref={mobileRef}
        className={`sm:hidden absolute top-16 left-0 w-full bg-[#120936] flex flex-col gap-2 p-4 shadow-lg transition-all duration-200 ${
          mobileMenu
            ? "opacity-100 translate-y-0 visible"
            : "opacity-0 -translate-y-2 invisible"
        }`}
      >
        {user && (
          <div className="flex items-center gap-3 p-3 bg-[#3b1f5c] rounded-md">
            <img
              src={user.profilePicture || Anonymous}
              alt="Profile"
              className="w-8 h-8 rounded-full border-2 border-transparent"
            />
            <div className="text-sm">
              <div className="font-semibold">{user.username || "null"}</div>
              <div>{user.email || "null"}</div>
            </div>
          </div>
        )}

        {navLinks.map((link) => (
          <Link
            key={link.path}
            to={link.path}
            onClick={() => setMobileMenu(false)}
            className={`px-4 py-2 rounded-md font-medium transition ${
              currentPath === link.path
                ? "bg-[#b588d5]"
                : "hover:bg-[#3b1f5c] hover:text-white"
            }`}
          >
            {link.name}
          </Link>
        ))}

        {user ? (
          <div className="flex flex-col gap-2 mt-2">
            <Link
              to="/settings"
              onClick={() => setMobileMenu(false)}
              className="px-4 py-2 bg-[#795493] rounded-md text-center hover:bg-[#633d7a] transition"
            >
              User Settings
            </Link>
            <button
              onClick={handleLogout}
              className="px-4 py-2 bg-[#795493] rounded-md text-center hover:bg-[#633d7a] transition"
            >
              Logout
            </button>
          </div>
        ) : (
          <Link
            to="/auth"
            onClick={() => setMobileMenu(false)}
            className="px-4 py-2 bg-[#b588d5] rounded-md text-center hover:bg-[#a26ac2] transition mt-2"
          >
            Sign Up / Login
          </Link>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
