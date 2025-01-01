import React, { useState } from "react";
import { Layout, ConfigProvider, theme } from "antd";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import ClanAdminPage from "./pages/ClanAdminPage";
import CustomHeader from "./components/Header";
import Sidebar from "./components/Sidebar";
import "@ant-design/v5-patch-for-react-19";
import Debug from "./pages/Debug";

const { Content } = Layout;

const checkPermission = (role) => {
  const userRole = "user";
  return role === userRole;
};

const LazyAdmin = React.lazy(() => import("./pages/Admin"));

const App = () => {
  const [collapsed, setCollapsed] = useState(false);

  return (
    <Router>
      <ConfigProvider
        theme={{
          algorithm: theme.darkAlgorithm,
          components: {
            Layout: {
              siderBg: "#141414",
              headerBg: "#141414",
              triggerBg: "#0f0f0f",
              bodyBg: "#0c0c0c",
            },
            Menu: {
              darkItemBg: "#141414",
              darkSubMenuItemBg: "#0f0f0f",
            },
          },
        }}
      >
        <Layout style={{ minHeight: "100vh" }}>
          <Sidebar
            collapsed={collapsed}
            onCollapse={(value) => setCollapsed(value)}
          />
          <Layout>
            <CustomHeader />
            <Content
              style={{
                margin: "16px",
                padding: "16px",
                borderRadius: "8px",
              }}
            >
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
                  <Route path="/debug" element={<Debug />} />
                </Routes>
              </React.Suspense>
            </Content>
          </Layout>
        </Layout>
      </ConfigProvider>
    </Router>
  );
};

export default App;
