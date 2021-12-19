import { HashRouter } from "react-router-dom";
import MainLayout from "../MainLayout";
import PatientsTable from "../components/PatientsTable";

const MainPage = () => {

  console.log("wtf");
    return (
      <div className="main-window">
        <div className="sidebar-container">
        <MainLayout/>
        <PatientsTable/>
        </div>
        <div className="content-container">
          
        </div>
      </div>

  )
}

export default MainPage