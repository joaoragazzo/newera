import React, { useEffect, useState } from "react";
import { Layout, Badge, Dropdown, List, Button, message } from "antd";
import { BellOutlined, CheckOutlined, CloseOutlined, RocketFilled } from "@ant-design/icons";
import { NotificationRoot } from "./notification/NotificationRoot";
import { NotificationContent } from "./notification/NotificationContent";
import { NotificationIcon } from "./notification/NotificationIcon";
import { NotificationActions } from "./notification/NotificationActions";
import { NotificationAction } from "./notification/NotificationAction";
import axios from "axios";

const { Header } = Layout;

const CustomHeader = () => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("/api//notification/stream");

    eventSource.onmessage = (event) => {
      const newNotification = JSON.parse(event.data);
      if (newNotification.title !== "Ping") {
        setNotifications(
          (prev) => [newNotification, ...prev]
        );
      }
    };

    eventSource.onerror = () => {
      message.error("Erro ao conectar ao servidor de notificações.");
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, []);


  const fetchNotifications = async () => {
    try {
      const response = await axios.post("/api/notification/list");
      setNotifications(response.data)
    } catch (error) {
      message.error(error.response.data.error)
    } 
  }

  useEffect(
    () => {fetchNotifications()}, 
    []);

  const notificationMenu = (
    <div
      style={{
        padding: "10px",
        borderRadius: "8px",
        boxShadow: "0 4px 12px rgba(0, 0, 0, 0.5)",
        maxWidth: "400px",
        backgroundColor: "#1f1f1f"
      }}
    >      
      <List
        dataSource={notifications}
        renderItem={(item) => (
          <NotificationRoot>
            <NotificationIcon icon={RocketFilled}/>
            <NotificationContent title={item.title} description={item.message} />
            <NotificationActions>
              {item.callback_for_accept && <NotificationAction icon={CheckOutlined} callback_url={item.callback_for_accept} />}
              {item.callback_for_decline && <NotificationAction icon={CloseOutlined} callback_url={item.callback_for_decline} />}
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
                style={{ fontSize: "20px" }}
              />
            }
          />
        </Badge>
      </Dropdown>
    </Header>
  );
};

export default CustomHeader;
