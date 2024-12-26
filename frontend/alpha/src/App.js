import React, { useState } from "react";
import { Layout, ConfigProvider, theme } from "antd";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import ClanAdminPage from "./components/pages/ClanAdminPage";
import CustomHeader from "./components/Header";
import Sidebar from "./components/Sidebar";

const { Content } = Layout;

const checkPermission = (role) => {
  const userRole = "user"; 
  return role === userRole;
};

const LazyAdmin = React.lazy(() => import("./components/pages/Admin"));

const App = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [darkMode, setDarkMode] = useState(true);

  return (
    <ConfigProvider theme={{ algorithm: darkMode ? theme.darkAlgorithm : theme.defaultAlgorithm }}>
      <Layout style={{ minHeight: "100vh" }}>
        <Sidebar
          collapsed={collapsed}
          onCollapse={(value) => setCollapsed(value)}
          darkMode={darkMode}
        />
        <Layout>
          <CustomHeader
            title={"Clan Administration"}
            darkMode={darkMode}
            toggleTheme={() => setDarkMode(!darkMode)}
          />
          <Content
            style={{
              margin: "16px",
              padding: "16px",
              background: darkMode ? "#141414" : "#fff",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            }}
          >
            <Router>
              <React.Suspense fallback={<div>Carregando...</div>}>
                <Routes>
                  <Route path="/clan" element={<ClanAdminPage />} />

                  <Route
                    path="/admin"
                    element={
                      checkPermission("admin") ? (
                        <LazyAdmin />
                      ) : (
                        <Navigate to="/clan" />
                      )
                    }
                  />
                </Routes>
              </React.Suspense>
            </Router>
          </Content>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
};

export default App;
