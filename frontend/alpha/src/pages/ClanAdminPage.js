import React, { useState, useEffect } from "react";
import { Typography, Table, Button, Tag, Space, Row, Col, Card, Progress, Tooltip, message, Modal, Form, Input, ColorPicker, Select } from "antd";
import axios from "axios";
import {
  UserOutlined,
  StarFilled,
  ArrowUpOutlined,
  ArrowDownOutlined,
  FileAddFilled,
  QuestionCircleOutlined
} from "@ant-design/icons";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import '@ant-design/v5-patch-for-react-19';
import FormItem from "antd/es/form/FormItem";
import EditClanButton from "../components/clan/EditClanButton";
import InviteClanButton from "../components/clan/InviteClanButton";

const { Title, Paragraph } = Typography;

const ClanAdminPage = () => {

  const [isCreateModalVisible, setIsCreateModalVisible] = useState(false);
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);
  const [isInviteModalVisible, setIsInviteModalVisible] = useState(false);

  const [createForm] = Form.useForm();
  const [editForm] = Form.useForm();
  const [inviteForm] = Form.useForm();

  const [users, setUsers] = useState([]);
  const [clanName, setClanName] = useState();
  const [clanTag, setClanTag] = useState();
  const [clanColor, setClanColor] = useState();
  const [clanOwner, setClanOwner] = useState(false);
  const [clanAdmin, setClanAdmin] = useState(false);
  const [playersToInvite, setPlayersToInvite] = useState([]);

  const [memberOfAClan, setMemberOfAClan] = useState(false);

  const getPlayers = async () => {
    try {
      const response = await axios.get("/api/player/list");
      const data = response.data;

      const formattedOptions = Object.entries(data).map(([player_id, name]) => ({
        value: player_id,
        label: name,
      }));

      setPlayersToInvite(formattedOptions);
    } catch (error) {
      console.error("Erro ao buscar jogadores:", error);
    }
  };

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

  const inviteClan = async (values) => {
    try {
      const response = await axios.post("/api/clan/invite", values);
      setIsInviteModalVisible(false);
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

        if (response.data.role == "ADMIN")
          setClanAdmin(true)

        if (response.data.role == "OWNER")
          setClanOwner(true)
      }

    } catch (error) {

    }

  }

  useEffect(() => {
    fetchInfo();
  }, []);

  useEffect(() => {
    getPlayers();
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

  const showInviteModal = () => {
    setIsInviteModalVisible(true)
  }

  const closeInviteModal = () => {
    setIsInviteModalVisible(false)
  }

  const onCreateOk = () => {
    createForm.submit();
  }

  const onEditOk = () => {
    editForm.submit();
  }

  const onInviteOk = () => {
    inviteForm.submit()
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

        <Card style={{ textAlign: "center" }} >
          <Title level={2}>Você não pertence a nenhum clan atualmente!</Title>
          <Button size="large" icon={<FileAddFilled />} type="primary" style={{ width: "45%" }} onClick={showCreateModal} > Criar um novo clan agora </Button>
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

  const InviteToClanCard = () => {

    return (
      <Modal
        title={`Convidar novos membros para ${clanName}`}
        open={isInviteModalVisible}
        onCancel={closeInviteModal}
        okText="Convidar"
        cancelText="Cancelar"
        onOk={onInviteOk}
      >
        <Form
          layout="vertical"
          id="form-clan"
          form={inviteForm}
          onFinish={inviteClan}
        >
          <Form.Item label="Jogador" name="invitee_id" rules={[
            { required: true, message: "Você precisa escolher o jogador a ser convidado." },
          ]}>
            <Select optionFilterProp="children" options={playersToInvite}
              filterOption={(input, option) =>
                option?.children.toLowerCase().includes(input.toLowerCase())
              } >

            </Select>
          </Form.Item>
        </Form>
      </Modal >
    );
  }

  const CreateGraphsCard = () => {
    return (
      <Row gutter={[16, 16]} style={{ marginBottom: "20px" }}>
        <Col span={8}>
          <Card
            title={<>
              Membros ativos
              <Tooltip title="Este card exibe os membros que estão ativos no momento.">
                <QuestionCircleOutlined style={{ marginLeft: "10px" }} />
              </Tooltip>
            </>}
            style={{
              borderRadius: "8px",
            }}
          >
            <Progress type="circle" percent={0} />
          </Card>
        </Col>
        <Col span={8}>
          <Card
            title={<>
              Tamanho do clan
              <Tooltip title="Este card exibe a quantidade de usuários no clan.">
                <QuestionCircleOutlined style={{ marginLeft: "10px" }} />
              </Tooltip>
            </>}
            style={{
              borderRadius: "8px",
            }}
          >
            <Progress type="circle" percent={100} format={() => users.length} />
          </Card>
        </Col>
        <Col span={8}>
          <Card
            title={<>
              Taxa de atividade
              <Tooltip title="Esse card exibe a atividade do clan. A atividade é mesurada a dividindo a somatória do tempo online total do clan com o tempo total.">
                <QuestionCircleOutlined style={{ marginLeft: "10px" }} />
              </Tooltip>
            </>}
            style={{
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
            }}
          >
            <Progress type="circle" percent={0} />
          </Card>
        </Col>
      </Row>
    );
  }

  const CreateMembersListCard = () => {
    return (
      <>
        {clanOwner && <EditClanCard />}
        {(clanAdmin || clanOwner) && <InviteToClanCard />}

        <Card
          title={<Title level={3}>[<span style={{ color: clanColor }}>{clanTag}</span>] {clanName}</Title>}
          extra={
            <>
              {clanOwner && <EditClanButton showEditModal={showEditModal} />}
              {(clanAdmin || clanOwner) && <InviteClanButton showInviteModal={showInviteModal} />}
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
