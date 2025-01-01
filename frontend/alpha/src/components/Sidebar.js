import React from "react";
import { Layout, Menu } from "antd";
import {
  TeamOutlined,
  UserOutlined,
  SettingOutlined,
  FolderFilled,
  QuestionCircleFilled,
  ShopOutlined,
  ShoppingCartOutlined,
  ShoppingFilled,
  BugFilled

} from "@ant-design/icons";
import { Typography } from 'antd';
import { useNavigate, useLocation } from "react-router-dom";

const { Title } = Typography;
const { Sider } = Layout;

const Sidebar = ({ collapsed, onCollapse }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleMenuClick = (item) => {
    navigate(item.key); 
  };

  return (
    <Sider
      collapsible
      collapsed={collapsed}
      onCollapse={onCollapse}
    >
      <div
        style={{
          height: "64px",
          textAlign: "center",
          lineHeight: "64px",
          fontWeight: "bold",
          fontSize: "18px",
        }}
      >
        {!collapsed ? (
          <Title level={2} style={{ margin: 20 }}>NEW ERA</Title>
        ) : (
          <Title level={5} style={{ margin: 20 }}>NEW ERA</Title>
        )}
      </div>
      <Menu
        defaultSelectedKeys={[location.pathname]}
        onClick={handleMenuClick}
        mode="inline"
        theme="dark"
        items={[
          {
            key: "1", label: "Servidor", children: [
              { key: "1-1", label: "Status do servidor" },
              { key: "1-2", label: "Regras"},
              { key: "1-3", label: "Equipe da administração" } 
            ]
          },
          {
            key: "2", icon: <ShopOutlined />, label: "Doações", children: [
              { key: "2-1", icon: <ShoppingFilled />, label: "Loja" },
              { key: "2-2", icon: <ShoppingCartOutlined />, label: "Carrinho" }
            ]
          },
          {
            key: "3", icon: <FolderFilled />, label: "Inventário", children: [
              { key: "3-1", label: "Consumíveis" },
              { key: "3-2", label: "Itens permanentes" },
              { key: "3-3", label: "Benefícios" }
            ]
          },
          { key: "/clan", icon: <TeamOutlined />, label: "Clan" },
          { key: "5", icon: <UserOutlined />, label: "Conta" },
          { key: "6", icon: <QuestionCircleFilled />, label: "Suporte" },
          { key: "7", icon: <SettingOutlined />, label: "Configurações" },
          { key: "/debug", icon: <BugFilled />, label: "Debug" }
        ]}
      />
    </Sider>
  );
};

export default Sidebar;
