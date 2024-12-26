import React, { useState } from "react";
import { Table, Button, Tag, Space, Row, Col, Card, Progress, message } from "antd";
import axios from "axios"; 
import {
  UserOutlined,
  StarFilled,
  PlusOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined,
  EditFilled
} from "@ant-design/icons";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import "../../App.css";

const ClanAdminPage = () => {
  const [users, setUsers] = useState([
    { id: 1, name: "John Doe", rank: "Dono", kills: 10, deaths: 5, kdr: 2.0, lastSeenAt: "2024-12-23T18:30:00" },
    { id: 2, name: "Jane Smith", rank: "Admin", kills: 10, deaths: 10, kdr: 1.0, lastSeenAt: "2024-12-23T18:30:00" },
    { id: 3, name: "Bob Johnson", rank: "Membro", kills: 5, deaths: 10, kdr: 0.5, lastSeenAt: "2024-12-23T18:30:00" },
    { id: 4, name: "John Doe", rank: "Membro", kills: 10, deaths: 5, kdr: 2.0, lastSeenAt: "2024-12-23T18:30:00" },
    { id: 5, name: "Jane Smith", rank: "Membro", kills: 10, deaths: 10, kdr: 1.0, lastSeenAt: "2024-12-23T18:30:00" },
    { id: 6, name: "Bob Johnson", rank: "Membro", kills: 5, deaths: 10, kdr: 0.5, lastSeenAt: "2024-12-23T18:30:00" }
  ]);

  // Função para promover jogador
  const promotePlayer = async (id) => {
    try {
      const response = await axios.post(`/api/promote`, { playerId: id });
      if (response.status === 200) {
        message.success("Jogador promovido com sucesso!");
        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === id ? { ...user, rank: user.rank === "Membro" ? "Admin" : "Dono" } : user
          )
        );
      } else {
        message.error("Erro ao promover jogador.");
      }
    } catch (error) {
      console.error(error);
      message.error("Erro na comunicação com o servidor.");
    }
  };

  const columns = [
    { title: <>< UserOutlined /> Nome </>, dataIndex: "name", key: "name" },
    {
      title: <>< StarFilled /> Cargo </>,
      dataIndex: "rank",
      key: "rank",
      render: (text) => (
        <Tag color={text === "Dono" ? "red" : text === "Admin" ? "gold" : "green"}>
          {text}
        </Tag>
      ),
    },
    {
      title: "Matou",
      dataIndex: "kills",
      key: "kills"
    },
    {
      title: "Morreu",
      dataIndex: "deaths",
      key: "deaths"
    },
    {
      title: "KDR",
      dataIndex: "kdr",
      key: "kdr"
    },
    {
      title: "Visto por último",
      dataIndex: "lastSeenAt",
      key: "lastSeenAt",
      render: (date) => {
        return format(new Date(date), "dd 'de' MMMM 'de' yyyy', às' HH:mm", { locale: ptBR });
      },
    },
    {
      title: "Ações",
      key: "actions",
      align: "right",
      render: (_, record) => (
        <Space>
          {
            record.rank === "Membro" ?
              (
                <Button icon={<ArrowUpOutlined />} onClick={() => promotePlayer(record.id)}>
                  Promover
                </Button>
              )
              : null
          }
          {
            record.rank == "Admin" ?
              (
                <Button icon={<ArrowDownOutlined />}>
                  Rebaixar
                </Button>
              )
              : null
          }
          {
            record.rank == "Membro" || record.rank == "Admin" ?
              (
                <Button danger>Expulsar</Button>
              )
              : null
          }

        </Space>
      ),
    },
  ];

  return (
    <div>
      {/* Gráficos */}
      <Row gutter={[16, 16]} style={{ marginBottom: "20px" }}>
        <Col span={8}>
          <Card
            title="Online Members"
            bordered={false}
            style={{
              background: "#1f1f1f",
              color: "#fff",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
            }}
          >
            <Progress type="circle" percent={75} />
          </Card>
        </Col>
        <Col span={8}>
          <Card
            title="Total Members"
            bordered={false}
            style={{
              background: "#1f1f1f",
              color: "#fff",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
            }}
          >
            <Progress type="circle" percent={100} format={() => users.length} />
          </Card>
        </Col>
        <Col span={8}>
          <Card
            title="Activity Rate"
            bordered={false}
            style={{
              background: "#1f1f1f",
              color: "#fff",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
            }}
          >
            <Progress type="circle" percent={60} />
          </Card>
        </Col>
      </Row>

      {/* Tabela de Usuários */}
      <Card
        title="Clan Administration"
        extra={
          <>
            <Button type="primary" icon={<EditFilled />} style={{marginRight: "10px" }}>
              Editar clan
            </Button>
            <Button type="primary" icon={<PlusOutlined />}>
              Convidar novos membros
            </Button>
          </>
        }
        bordered={false}
        style={{
          background: "#1f1f1f",
          color: "#fff",
          borderRadius: "8px",
          boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
        }}
      >
        <Table
          dataSource={users.map((user) => ({ ...user, key: user.id }))}
          columns={columns}
          pagination={{ pageSize: 5 }}
        />
      </Card>
    </div>
  );
};

export default ClanAdminPage;