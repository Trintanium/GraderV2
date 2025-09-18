import { Outlet } from "react-router-dom";
import Navbar from "@/components/auth/Navbar";

function App() {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <div className="flex-1 flex justify-center items-center">
        <Outlet />
      </div>
    </div>
  );
}

export default App;
