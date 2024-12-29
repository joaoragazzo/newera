import React, { useState, useEffect } from "react";
import { Typography, Table, Button, Tag, Space, Row, Col, Card, Progress, message, Modal, Form, Input, ColorPicker } from "antd";
import axios from "axios";
import {
  UserOutlined,
  StarFilled,
  PlusOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined,
  EditFilled,
  FileAddFilled
} from "@ant-design/icons";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import '@ant-design/v5-patch-for-react-19';
import FormItem from "antd/es/form/FormItem";

const { Title, Paragraph } = Typography;



const ClanAdminPage = () => {

  const [form] = Form.useForm();
  const [users, setUsers] = useState([]);
  const [clanName, setClanName] = useState();
  const [clanTag, setClanTag] = useState();
  const [clanColor, setClanColor] = useState();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [memberOfAClan, setMemberOfAClan] = useState(false);

  const fetchInfo = async () => {
    try {
      const response = await axios.post("/api/clan/info")

      if (response.data) {
        setMemberOfAClan(true);
        setUsers(response.data.members);
        setClanName(response.data.name);
        setClanTag(response.data.tag);
        setClanColor(response.data.color);
      }

    } catch (error) {

    }

  }

  useEffect(() => {
    fetchInfo();
  }, []);

  const showModal = () => {
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setIsModalVisible(false);
  }

  const createClan = async (values) => {
    try {
      const response = await axios.post("/api/clan/create", values);
      setIsModalVisible(false);
      setMemberOfAClan(true);
      await fetchInfo();
      message.success(response.data.success)
    } catch (error) {
      message.error(error.response.data.error)
    }
  }

  const onOk = () => {
    form.submit();
  }

  const CreateClanCard = () => {
    return (
      <>
        <Modal
          title="Criação de um novo clan"
          open={isModalVisible}
          onCancel={closeModal}
          okText="Criar clan"
          cancelText="Deixar para depois"
          onOk={onOk}>
          <Form layout="vertical" id="form-clan" form={form} onFinish={createClan}>
            <Form.Item label="Nome do clan" name="name" rules={[
              { required: true, message: "Você precisa escolher um nome de um clan." },
              { max: 32, message: "O nome do clan deve ser no máximo 32 caracteres." }
            ]}>
              <Input></Input>
            </Form.Item>
            <FormItem label="Tag do clan" name="tag" rules={[
              { required: true, message: "Você precisa escolher uma tag de um clan." },
              { min: 2, message: "A tag deve ter no mínimo 2 caracteres." },
              { max: 4, message: "A tag deve ter no máximo 4 caracteres." }
            ]}>
              <Input></Input>
            </FormItem>
            <FormItem label="Selecione uma cor para representar o clan" name="color" >
              <ColorPicker onChange={(color) => {
                const hexColor = color.toHexString();
                form.setFieldValue("color", hexColor);
              }}></ColorPicker>
            </FormItem>
          </Form>
        </Modal>

        <Card style={{ textAlign: "center" }}>
          <Title level={2}>Você não pertence a nenhum clan atualmente!</Title>
          <Paragraph>Você pode criar um clan em poucos segundos ao clicar no botão abaixo</Paragraph>
          <Button icon={<FileAddFilled />} type="primary" style={{ width: "45%" }} onClick={showModal} > Criar um novo clan agora </Button>
        </Card>
      </>
    );
  }

  const CreateGraphsCard = () => {
    return (
      <Row gutter={[16, 16]} style={{ marginBottom: "20px" }}>
        <Col span={8}>
          <Card
            title="Online Members"
            bordered={false}
            style={{
              borderRadius: "8px",
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
              borderRadius: "8px",
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
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
            }}
          >
            <Progress type="circle" percent={60} />
          </Card>
        </Col>
      </Row>
    );
  }

  const CreateMembersListCard = () => {
    return (
      <Card
        title={<Title level={3}>[<span style={{ color: clanColor }}>{clanTag}</span>] {clanName}</Title>}
        extra={
          <>
            <Button type="primary" icon={<EditFilled />} style={{ marginRight: "10px" }}>
              Editar clan
            </Button>
            <Button type="primary" icon={<PlusOutlined />}>
              Convidar novos membros
            </Button>
          </>
        }
        bordered={false}
        style={{
          borderRadius: "8px",
        }}
      >
        <Table
          dataSource={users.map((user) => ({ ...user, key: user.id }))}
          columns={columns}
          pagination={{ pageSize: 5 }}
        />
      </Card>
    );
  }

  const columns = [
    { title: <>< UserOutlined /> Nome </>, dataIndex: "nick", key: "nick" },
    {
      title: <>< StarFilled /> Cargo </>,
      dataIndex: "role",
      key: "role",
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
      dataIndex: "last_seen_at",
      key: "last_seen_at",
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
                <Button icon={<ArrowUpOutlined />}>
                  Promover
                </Button>
              )
              : null
          }
          {
            record.rank === "Admin" ?
              (
                <Button icon={<ArrowDownOutlined />}>
                  Rebaixar
                </Button>
              )
              : null
          }
          {
            record.rank === "Membro" || record.rank === "Admin" ?
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
      <CreateGraphsCard />

      {!memberOfAClan ?
        <CreateClanCard />
        :
        <CreateMembersListCard />
      }

    </div>
  );
};

export default ClanAdminPage;
