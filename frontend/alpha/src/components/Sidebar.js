import React from "react";
import { FaCrown, FaInfo, FaChartPie } from "react-icons/fa6";
import { FaBoxes, FaInfoCircle } from "react-icons/fa";
import { MdDiscount } from "react-icons/md";
import { IoIosPeople } from "react-icons/io";
import { RiSettings3Fill } from "react-icons/ri";
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
  BugFilled,
  StarFilled

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
            key: "/admin", icon: <FaCrown />, label: "Administração", children: [
              { key: "/admin/info", icon: <FaInfoCircle /> ,label: "Informações do servidor" },
              { key: "/admin/statistics", icon: <FaChartPie />, label: "Estatísticas" },
              { key: "/admin/shop", icon: <ShopOutlined />, label: "Loja" },
              { key: "/admin/players", icon: <IoIosPeople />, label: "Jogadores" },
              { key: "/admin/system", icon: <RiSettings3Fill />, label: "Configurações do sistema" }
            ]
          },
          {
            key: "/", label: "Servidor", icon: <StarFilled /> ,children: [
              { key: "/status", label: "Status do servidor" },
              { key: "/rules", label: "Regras" },
              { key: "/staff", label: "Equipe da administração" }
            ]
          },
          {
            key: "/donate", icon: <ShopOutlined />, label: "Doações", children: [
              { key: "/donate/store", icon: <ShoppingFilled />, label: "Loja" },
              { key: "/donate/cart", icon: <ShoppingCartOutlined />, label: "Carrinho" }
            ]
          },
          {
            key: "/inventory", icon: <FolderFilled />, label: "Inventário", children: [
              { key: "", label: "Consumíveis" },
              { key: "/permanents", label: "Itens permanentes" },
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
