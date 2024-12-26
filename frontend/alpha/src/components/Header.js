import React, { useState } from "react";
import { Layout, Badge, Dropdown, List, Button } from "antd";
import { BellOutlined, CheckOutlined, CloseOutlined, RocketFilled } from "@ant-design/icons";
import { NotificationRoot } from "./notification/NotificationRoot";
import { NotificationContent } from "./notification/NotificationContent";
import { NotificationIcon } from "./notification/NotificationIcon";
import { NotificationActions } from "./notification/NotificationActions";
import { NotificationAction } from "./notification/NotificationAction";

const { Header } = Layout;

const CustomHeader = ({ darkMode, title }) => {
  const [notifications, setNotifications] = useState([
    {
      id: 1,
      title: "Você foi convidado",
      description: "Clique para aceitar ou recusar o convite. Lorem ipsum dolor sit amet lorem ipsum",
    },
    {
      id: 2,
      title: "Nova mensagem",
      description: "Você tem uma nova mensagem.",
    },
    {
      id: 3,
      title: "Evento de clã",
      description: "Participe do evento amanhã às 20h.",
    },
  ]);

  const handleAction = (id, action) => {
    console.log(`Notification ${id} ${action === "accept" ? "accepted" : "rejected"}`);
    setNotifications((prev) => prev.filter((notif) => notif.id !== id));
  };

  const notificationMenu = (
    <div
      style={{
        background: darkMode ? "#1f1f1f" : "#fff",
        padding: "10px",
        borderRadius: "8px",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.5)",
        maxWidth: "400px",
      }}
    >      
      <List
        dataSource={notifications}
        renderItem={(item) => (
          <NotificationRoot>
            <NotificationIcon icon={RocketFilled}/>
            <NotificationContent title={item.title} description={item.description} />
            <NotificationActions>
              <NotificationAction icon={CheckOutlined} />
            </NotificationActions>
          </NotificationRoot>
        )}
        locale={{ emptyText: "Sem notificações" }}
      />
    </div>
  );

  return (
    <Header
      style={{
        background: darkMode ? "#1f1f1f" : "#f0f2f5",
        display: "flex",
        justifyContent: "right",
        alignItems: "center",
        padding: "0 20px",
      }}
    >
      <Dropdown
        overlay={notificationMenu}
        trigger={["click"]}
        placement="bottomRight"
        arrow
      >
        <Badge
          count={notifications.length}
          offset={[-5, 5]}
        >
          <Button
            type="text"
            icon={
              <BellOutlined
                style={{ fontSize: "20px", color: darkMode ? "#fff" : "#000" }}
              />
            }
          />
        </Badge>
      </Dropdown>
    </Header>
  );
};

export default CustomHeader;
