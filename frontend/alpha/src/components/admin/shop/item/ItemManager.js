import React, { useEffect, useRef, useState } from "react";
import { Image, Table, Input, Button, Space, Modal, Form, Col, Row, Upload, Select, Cascader, InputNumber, Switch, Card, Divider, message } from "antd";
import { DeleteOutlined, EditOutlined, EyeOutlined, InboxOutlined, MinusCircleOutlined, PlusOutlined, SearchOutlined, ShoppingCartOutlined } from "@ant-design/icons";
import { IoAddCircleOutline } from "react-icons/io5";
import TextArea from "antd/es/input/TextArea";
import PriceNameWithTooltip from "./components/tooltips/PriceNameWithTooltip";
import YoutubeNameWithTooltip from "./components/tooltips/YoutubeNameWithTooltip";
import TagsNameWithTooltip from "./components/tooltips/TagsNameWithTooltop";
import NotifySiteWithTooltip from "./components/tooltips/NotifySiteNameWithTooltip";
import AllowCommentsNameWithTooltip from "./components/tooltips/AllowCommentsNameWithTooltip";
import NotifyPrivateDiscord from "./components/tooltips/NotifyPrivateDiscord";
import NotifyPublicDiscord from "./components/tooltips/NotifyPublicDiscord";
import AllowReviewWithTooltip from "./components/tooltips/AllowReviewNameWithTooltip";
import ShowAcquisitionWithTooltip from "./components/tooltips/ShowAcquisitionWithTooltip";
import StockNumberNameWithTooltip from "./components/tooltips/StockNumberNameWithTooltip";
import axios from "axios";

const ItemManager = () => {

    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [thumbnail, setThumbnail] = useState();
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const searchInput = useRef(null);
    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [addItemForm] = Form.useForm();
    const [category, setCategories] = useState([]);

    const fetchCategories = async () => {
        try {
            const response = await axios.post("/api/admin/shop/fetch/category")
            const data = response.data
            setCategories(data);
        } catch (error) {
            message.error(error.response.data.error)
        }
    }

    useEffect(() => {
        fetchCategories();
    },[]);

    const tags = []

    const getBase64 = (file) =>
        new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = (error) => reject(error);
        });

    const [fileList, setFileList] = useState([]);

    const handlePreview = async (file) => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj);
        }

        setPreviewImage(file.url || (file.preview));
        setPreviewOpen(true);
    };

    const handleChange = ({ fileList: newFileList }) =>
        setFileList(newFileList);

    const uploadButton = (
        <button style={{ border: 0, background: 'none', color: 'white', cursor: "pointer" }} type="button">
            <PlusOutlined />
            <div style={{ marginTop: 8, color: 'white', cursor: "pointer" }}>Upload</div>
        </button>
    );

    const suffixSelector = (
        <Form.Item name="type" noStyle rules={[
            { required: true, message: "Por favor, selecione o tipo de benefício da doação" }
        ]}>
            <Select style={{ width: 150 }}>
                <Select.Option value="UNIQUE">por uso</Select.Option>
                <Select.Option value="DAILY">por dia</Select.Option>
                <Select.Option value="WEEKLY">por semana</Select.Option>
                <Select.Option value="MONTH">por mês</Select.Option>
                <Select.Option value="WIPE">por wipe</Select.Option>
                <Select.Option value="ANUAL">por ano</Select.Option>
                <Select.Option value="PERMA">permanente</Select.Option>
            </Select>
        </Form.Item>
    );

    const onOkAddFile = () => {
        addItemForm.submit()
    }

    const onFinishAddForm = async (values) => {
        try {
            const response = await axios.post("/api/admin/shop/create/item", values)
            const data = response.data;
            message.success(data.success)
            addItemForm.resetFields()
            setIsAddModalVisible(false)
        } catch (error) {
            message.error(error.response.data.error)
        }
    }

    const AddModal = () => {
        return (
            <Modal
                title="Criar um novo item"
                open={isAddModalVisible}
                onCancel={() => { setIsAddModalVisible(false) }}
                okText="Criar item"
                width={900}
                onOk={onOkAddFile}
            >
                <Form
                    layout="vertical"
                    form={addItemForm}
                    onFinish={onFinishAddForm}
                    initialValues={{
                        notifySite: false,
                        notifyPublicDiscord: false,
                        notifyPrivateDiscord: false,
                        allowComments: false,
                        allowRating: false,
                        showAcquisition: false,
                        manualDelivery: false,
                        tags: [],
                        type: "UNIQUE"
                    }}
                >
                    <Row gutter={30}>
                        <Col span={14}>
                            <Row>
                                <Form.Item label="Nome do item" name="name" style={{ width: "100%" }} rules={[
                                    { required: true, message: "Por favor, insira um nome para o item" },
                                    { max: 64, message: "O nome não pode passar de 64 caracteres. " },
                                ]}>
                                    <Input placeholder="Nome do item" maxLength={64} showCount={true} />
                                </Form.Item>
                            </Row>
                            <Row>
                                <Form.Item label="Categoria" name="category" style={{ width: "100%" }} rules={[
                                    { required: true, message: "Por favor, insira a categoria para o item" }
                                ]}>
                                    <Cascader options={category}
                                        changeOnSelect
                                        placeholder="Escolha uma categoria" />
                                </Form.Item>
                            </Row>
                            <Row>
                                <Form.Item label={<PriceNameWithTooltip />} name="price" style={{ width: "100%" }} rules={[
                                    { required: true, message: "Por favor, insira um preço para o item" }
                                ]}>
                                    <InputNumber
                                        style={{ width: "100%" }}
                                        addonAfter={suffixSelector}
                                        prefix={"R$"}
                                        formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, '.')}
                                        parser={(value) => value?.replace(/\$\s?|(\.*)/g, '')}

                                    />
                                </Form.Item>
                            </Row>
                        </Col>
                        <Col>
                            <Form.Item
                                label="Imagem da loja"
                                name="thumbnail"
                                rules={[
                                    { required: true, message: "Por favor, insira a thumbnail" }
                                ]}
                            >
                                <Upload.Dragger
                                    name="files"
                                    maxCount={1}
                                    showUploadList={false}
                                    beforeUpload={(file) => {
                                        const reader = new FileReader();
                                        reader.onload = (e) => {
                                            setThumbnail(e.target.result);
                                            addItemForm.setFieldsValue({ thumbnail: e.target.result });
                                        };
                                        reader.readAsDataURL(file);
                                        return false;
                                    }}
                                    style={{
                                        width: 300,
                                        height: 300,
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center",
                                    }}>
                                    {
                                        thumbnail ?
                                            (
                                                <Image
                                                    src={thumbnail}
                                                    alt="Thumbnail"
                                                    style={{
                                                        maxWidth: "100%",
                                                        maxHeight: "100%",
                                                        objectFit: "contain",
                                                    }}
                                                />
                                            ) :
                                            (
                                                <>
                                                    <p className="ant-upload-drag-icon">
                                                        <InboxOutlined />
                                                    </p>
                                                    <p className="ant-upload-text">Clique aqui ou arraste uma foto</p>
                                                    <p className="ant-upload-hint">Suporte apenas para um único upload.</p>
                                                </>
                                            )
                                    }

                                </Upload.Dragger>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={14}>
                            <Form.Item label={<TagsNameWithTooltip />} name="tags" >
                                <Select
                                    mode="multiple"
                                    placeholder="Tags associadas"
                                    style={{ width: '100%' }}
                                    options={tags}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={10}>
                            <Form.Item label={< StockNumberNameWithTooltip />} name="stock" >
                                <InputNumber style={{ width: '100%' }} />
                            </Form.Item>
                        </Col>

                    </Row>
                    <Row>
                        <Form.Item label="Descrição" name="description" style={{ width: "100%" }} rules={[
                            { required: true, message: "Por favor, insira uma descrição para o produto" }
                        ]}>
                            <TextArea showCount maxLength={1000} />
                        </Form.Item>
                    </Row>
                    <Row>
                        <Form.Item label={< YoutubeNameWithTooltip />}>
                            <Form.List name="youtubeLinks">
                                {(fields, { add, remove }) => (
                                    <>
                                        <Col>
                                            {fields.map(({ key, name, fieldKey, ...restField }) => (
                                                <Space
                                                    key={key}
                                                    style={{ display: "flex", marginBottom: 8 }}
                                                    align="baseline"
                                                >
                                                    <Form.Item
                                                        {...restField}
                                                        name={[name]}
                                                        fieldKey={[fieldKey]}
                                                        rules={[
                                                            { required: true, message: "Por favor, insira o link do YouTube!" },
                                                            { type: "url", message: "Insira um URL válido!" },
                                                        ]}
                                                    >
                                                        <Input placeholder="Insira o link do YouTube" />
                                                    </Form.Item>
                                                    <MinusCircleOutlined
                                                        onClick={() => remove(name)}
                                                        style={{ color: "red" }}
                                                    />
                                                </Space>
                                            ))}
                                            <Form.Item>
                                                <Button
                                                    type="dashed"
                                                    onClick={() => add()}
                                                    block
                                                    icon={<PlusOutlined />}
                                                >
                                                    Adicionar outro link
                                                </Button>
                                            </Form.Item>
                                        </Col>
                                    </>

                                )}
                            </Form.List>
                        </Form.Item>
                    </Row>
                    <Row gutter={[16, 16]}>
                        <Col span={24}>
                            <Card title="Notificações">
                                <Row>
                                    <Col span={12}>
                                        <Form.Item name="notifySite" label={<NotifySiteWithTooltip />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item name="notifyPublicDiscord" label={<NotifyPublicDiscord />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item name="notifyPrivateDiscord" label={<NotifyPrivateDiscord />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                </Row>
                            </Card>
                        </Col>
                        <Col span={24}>
                            <Card title="Interações">
                                <Row>
                                    <Col span={12}>
                                        <Form.Item name="allowComments" label={<AllowCommentsNameWithTooltip />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item name="allowRating" label={< AllowReviewWithTooltip />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item name="showAcquisition" label={< ShowAcquisitionWithTooltip />} >
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                </Row>
                            </Card>
                        </Col>
                        <Col span={24}>
                            <Card title="Entrega">
                                <Row>
                                    <Col span={12}>
                                        <Form.Item name="manualDelivery" label="Entrega manual">
                                            <Switch />
                                        </Form.Item>
                                    </Col>
                                </Row>
                            </Card>
                        </Col>
                    </Row>

                    <Row>
                        <Divider />
                        <Form.Item label="Imagens adicionais" name="images" style={{ width: "100%" }}>
                            <Upload
                                listType="picture-card"
                                fileList={fileList}
                                beforeUpload={(file) => {
                                    const reader = new FileReader();
                                    reader.onload = (e) => {
                                        file.thumbUrl = e.target.result;

                                        const currentImages = addItemForm.getFieldValue('images') || [];
                                        const newImages = [...currentImages, e.target.result];
                                        addItemForm.setFieldsValue({ images: newImages });
                                    };
                                    reader.readAsDataURL(file);
                                    return false;
                                }}
                                onChange={({ fileList: newFileList }) => {
                                    setFileList(newFileList);
                                }}
                                onPreview={handlePreview}
                            >
                                {fileList.length >= 8 ? null : uploadButton}
                            </Upload>

                            {previewImage && (
                                <Image
                                    wrapperStyle={{ display: 'none' }}
                                    preview={{
                                        visible: previewOpen,
                                        onVisibleChange: (visible) => setPreviewOpen(visible),
                                        afterOpenChange: (visible) => !visible && setPreviewImage(''),
                                    }}
                                    src={previewImage}
                                />
                            )}
                        </Form.Item>
                    </Row>
                </Form>

            </Modal>
        )
    }

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };

    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText("");
    };

    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={searchInput}
                    placeholder={`Pesquisar pelo nome`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ marginBottom: 8, display: "block" }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Pesquisar
                    </Button>
                    <Button
                        onClick={() => clearFilters && handleReset(clearFilters)}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Resetar
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => (
            <SearchOutlined style={{ color: filtered ? "#1890ff" : undefined }} />
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        render: (text) =>
            searchedColumn === dataIndex ? (
                text
            ) : (
                text
            ),
    });

    const columns = [
        {
            title: "Nome",
            dataIndex: "item_name",
            key: "item_name",
            ...getColumnSearchProps("item_name"),
        },
        {
            title: "Categoria",
            dataIndex: "category",
            key: "category",
            filters: [
                { text: "Armas", value: "Armas" },
                { text: "Roupas", value: "Roupas" },
                { text: "Consumíveis", value: "Consumíveis" },
            ],
            onFilter: (value, record) => record.category === value,
        },
        {
            title: "Tipo",
            dataIndex: "type",
            key: "type",
            filters: [
                { text: "Permanente", value: "Permanente" },
                { text: "Temporário", value: "Temporário" },
                { text: "Consumível", value: "Consumível" },
            ],
            onFilter: (value, record) => record.type === value,
        },
        {
            title: "Tag",
            dataIndex: "tag",
            key: "tag",
        },
        {
            title: "Preço",
            dataIndex: "price",
            key: "price",
            sorter: (a, b) => a.price - b.price,
        },
        {
            title: "Ação",
            key: "action",
            width: "0",
            render: (_, record) => (
                <Space>
                    <Button type="dashed" icon={< ShoppingCartOutlined />} />
                    <Button type="dashed" icon={< EyeOutlined />} />
                    <Button type="dashed" icon={< EditOutlined />} />
                    <Button type="dashed" icon={< DeleteOutlined />} />
                </Space>
            )
        }
    ];

    const data = [];

    return (
        <>
            <AddModal thumbnail={thumbnail} />
            <div style={{ marginBottom: "20px", textAlign: "right" }}>
                <Button icon={<IoAddCircleOutline />} size="large" onClick={() => { setIsAddModalVisible(true) }}>Adicionar novo item</Button>
            </div>
            <Table columns={columns} dataSource={data} />
        </>
    );
};

export default ItemManager;
