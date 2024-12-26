import React from "react";
import { Layout, Menu } from "antd";
import {
  DashboardOutlined,
  TeamOutlined,
  UserOutlined,
  SettingOutlined,
  UserAddOutlined,
  FolderFilled,
  QuestionCircleFilled,
  ShopOutlined,
  ShoppingCartOutlined,
  ShoppingFilled

} from "@ant-design/icons";


const { Sider } = Layout;

const Sidebar = ({ collapsed, onCollapse, darkMode }) => {
  return (
    <Sider
      collapsible
      collapsed={collapsed}
      onCollapse={onCollapse}
      style={{
        background: darkMode ? "#1f1f1f" : "#f0f2f5",
        boxShadow: "2px 0 8px rgba(0, 0, 0, 0.1)",
      }}
    >
      <div
        style={{
          height: "64px",
          color: darkMode ? "#fff" : "#000",
          textAlign: "center",
          lineHeight: "64px",
          fontWeight: "bold",
          fontSize: "18px",
        }}
      >
        NEW ERA 
      </div>
      <Menu
        theme="dark"
        style={{
          background: darkMode ? "#1f1f1f" : "#f0f2f5",
          color: darkMode ? "#fff" : "#000",
        }}
        defaultSelectedKeys={["1"]}
        mode="inline"
        items={[
          { key: "2", icon: <ShopOutlined />, label: "Doações" , children: [
            { key: "2-1", icon: <ShoppingFilled />, label: "Loja", className: "bg-dark-2" },
            { key: "2-2", icon: <ShoppingCartOutlined />, label: "Carrinho", className: "bg-dark-2"}
          ]},
          { key: "3", icon: <FolderFilled />, label: "Inventário", children: [
            { key: "3-1", label: "Consumíveis" },
            { key: "3-2", label: "Itens permanentes" },
            { key: "3-3", label: "Benefícios" }
          ]},
          { key: "4", icon: <TeamOutlined />, label: "Clan" },
          { key: "5", icon: <UserOutlined />, label: "Conta" },
          { key: "6", icon: <QuestionCircleFilled />, label: "Suporte" },
          { key: "7", icon: <SettingOutlined />, label: "Configurações" },
        ]}
      />
    </Sider>
  );
};

export default Sidebar;
