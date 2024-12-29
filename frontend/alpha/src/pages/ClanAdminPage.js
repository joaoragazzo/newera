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

  const [isCreateModalVisible, setIsCreateModalVisible] = useState(false);
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);

  const [createForm] = Form.useForm();
  const [editForm] = Form.useForm();

  const [users, setUsers] = useState([]);
  const [clanName, setClanName] = useState();
  const [clanTag, setClanTag] = useState();
  const [clanColor, setClanColor] = useState();
  
  const [memberOfAClan, setMemberOfAClan] = useState(false);

  const createClan = async (values) => {
    try {
      const response = await axios.post("/api/clan/create", values);
      setIsCreateModalVisible(false);
      setMemberOfAClan(true);
      await fetchInfo();
      message.success(response.data.success)
    } catch (error) {
      message.error(error.response.data.error)
    }
  }

  const editClan = async (values) => {
    try {
      const response = await axios.post("/api/clan/edit", values);
      setIsEditModalVisible(false);
      await fetchInfo();
      message.success(response.data.success)
    } catch (error) {
      message.error(error.response.data.error)
    }
  }

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

  const showCreateModal = () => {
    setIsCreateModalVisible(true);
  };

  const closeCreateModal = () => {
    setIsCreateModalVisible(false);
  }

  const showEditModal = () => {
    setIsEditModalVisible(true)
  }

  const closeEditModal = () => {
    setIsEditModalVisible(false)
  }

  const onCreateOk = () => {
    createForm.submit();
  }

  const onEditOk = () => {
    editForm.submit();
  }

  const CreateClanCard = () => {
    return (
      <>
        <Modal
          title="Criação de um novo clan"
          open={isCreateModalVisible}
          onCancel={closeCreateModal}
          okText="Criar clan"
          cancelText="Deixar para depois"
          onOk={onCreateOk}>
          <Form layout="vertical" id="form-clan" form={createForm} onFinish={createClan}>
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
                createForm.setFieldValue("color", hexColor);
              }}></ColorPicker>
            </FormItem>
          </Form>
        </Modal>

        <Card style={{ textAlign: "center" }}>
          <Title level={2}>Você não pertence a nenhum clan atualmente!</Title>
          <Paragraph>Você pode criar um clan em poucos segundos ao clicar no botão abaixo</Paragraph>
          <Button icon={<FileAddFilled />} type="primary" style={{ width: "45%" }} onClick={showCreateModal} > Criar um novo clan agora </Button>
        </Card>
      </>
    );
  }

  const EditClanCard = () => {
    return (
      <Modal
        title={`Editar informações de ${clanName}`}
        open={isEditModalVisible}
        onCancel={closeEditModal}
        okText="Salvar edição"
        cancelText="Cancelar"
        onOk={onEditOk}
      >
        <Form
          layout="vertical"
          id="form-clan"
          form={editForm}
          onFinish={editClan}
          initialValues={{
            name: clanName,
            tag: clanTag,
            color: clanColor
          }}
        >
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
            <ColorPicker value={clanColor}
              onChange={(color) => {
                const hexColor = color.toHexString();
                editForm.setFieldValue("color", hexColor);
              }}></ColorPicker>
          </FormItem>
        </Form>
      </Modal>
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
      <>
        <EditClanCard />
        <Card
          title={<Title level={3}>[<span style={{ color: clanColor }}>{clanTag}</span>] {clanName}</Title>}
          extra={
            <>
              <Button type="primary" icon={<EditFilled />} style={{ marginRight: "10px" }} onClick={showEditModal}>
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
      </>
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
