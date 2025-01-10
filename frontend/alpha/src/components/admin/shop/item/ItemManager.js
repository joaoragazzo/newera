import React, { useRef, useState } from "react";
import { Image, Table, Input, Button, Space, Modal, Form, Col, Row, Upload, Select, Tooltip, Cascader, InputNumber, Switch } from "antd";
import { InboxOutlined, MinusCircleOutlined, PlusOutlined, SearchOutlined } from "@ant-design/icons";
import { IoAddCircleOutline } from "react-icons/io5";
import { Option } from "antd/es/mentions";
import TextArea from "antd/es/input/TextArea";
import PriceNameWithTooltip from "./components/PriceNameWithTooltip";
import YoutubeNameWithTooltip from "./components/YoutubeNameWithTooltip";

const ItemManager = () => {

    const categories = [
        {
            value: "armas",
            label: "Armas",
            isLeaf: false, // Supercategoria é selecionável
            children: [
                { value: "espadas", label: "Espadas" },
                { value: "arcos", label: "Arcos" },
                { value: "lanças", label: "Lanças" },
            ],
        },
        {
            value: "roupas",
            label: "Roupas",
            isLeaf: false,
            children: [
                { value: "capacetes", label: "Capacetes" },
                { value: "peitorais", label: "Peitorais" },
                { value: "botas", label: "Botas" },
            ],
        },
        {
            value: "consumiveis",
            label: "Consumíveis",
            isLeaf: false,
            children: [
                { value: "poções", label: "Poções" },
                { value: "alimentos", label: "Alimentos" },
            ],
        },
    ];

    const tags = [
        {
            value: "arma",
            label: "arma"
        },
        {
            value: "hitkill",
            label: "hitkill",
        },
        {
            value: "base",
            label: "base"
        }
    ]


    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [thumbnail, setThumbnail] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const searchInput = useRef(null);
    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');

    const getBase64 = (file) =>
        new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => resolve(reader.result);
          reader.onerror = (error) => reject(error);
        });

    const [fileList, setFileList] = useState([
        {
            uid: '-1',
            name: 'image.png',
            status: 'done',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-2',
            name: 'image.png',
            status: 'done',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-3',
            name: 'image.png',
            status: 'done',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-4',
            name: 'image.png',
            status: 'done',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-xxx',
            percent: 50,
            name: 'image.png',
            status: 'uploading',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-5',
            name: 'image.png',
            status: 'error',
        },
    ]);

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
        <Form.Item name="type" noStyle>
            <Select style={{ width: 150 }} defaultValue={"unique"}>
                <Option value="unique">por uso</Option>
                <Option value="daily">por dia</Option>
                <Option value="weekly">por semana</Option>
                <Option value="month">por mês</Option>
                <Option value="wipe">por wipe</Option>
                <Option value="anual">por ano</Option>
                <Option value="perma">permanente</Option>
            </Select>
        </Form.Item>
    );

    const AddModal = () => {
        return (
            <Modal
                title="Criar um novo item"
                open={isAddModalVisible}
                onCancel={() => { setIsAddModalVisible(false) }}
                okText="Criar item"
                width={800}
            >
                <Form layout="vertical">
                    <Row gutter={30}>
                        <Col span={14}>
                            <Row>
                                <Form.Item label="Nome do item" name="item_name" style={{ width: "100%" }} rules={[
                                    { required: true, message: "Por favor, insira um nome para o item" }
                                ]}>
                                    <Input placeholder="Nome do item" />
                                </Form.Item>
                            </Row>
                            <Row>
                                <Form.Item label="Categoria" name="category" style={{ width: "100%" }} rules={[
                                    { required: true, message: "Por favor, insira a categoria para o item" }
                                ]}>
                                    <Cascader options={categories}
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
                                        defaultValue={0}
                                        prefix={"R$"}
                                        formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, '.')}
                                        parser={(value) => value?.replace(/\$\s?|(\.*)/g, '')}

                                    />
                                </Form.Item>
                            </Row>
                        </Col>
                        <Col>
                            <Form.Item label="Imagem da loja" name="thumbnail" rules={[
                                { required: true, message: "Por favor, insira uma imagem demonstrativa do produto" }
                            ]}>
                                <Upload.Dragger name="files" action="/upload.do">
                                    <p className="ant-upload-drag-icon">
                                        <InboxOutlined />
                                    </p>
                                    <p className="ant-upload-text">Clique aqui ou arraste uma foto</p>
                                    <p className="ant-upload-hint">Suporte apenas para um único upload.</p>
                                </Upload.Dragger>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row>
                        <Form.Item label={<TagsNameWithTooltip />} name="tags" style={{ width: "100%" }}>
                            <Select
                                mode="multiple"
                                placeholder="Tags associadas"
                                style={{ width: '100%' }}
                                options={tags}
                            />
                        </Form.Item>
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
                    <Row gutter={30}>
                        <Col>
                            <Form.Item label="Notificar jogadores do site">
                                <Switch />
                            </Form.Item>
                        </Col>
                        <Col>
                            <Form.Item label="Notificar webhook do Discord">
                                <Switch />
                            </Form.Item>
                        </Col>
                        <Col>
                            <Form.Item label="Permitir comentários">
                                <Switch />
                            </Form.Item>
                        </Col>
                        <Col>
                            <Form.Item label="Permitir avaliações">
                                <Switch />
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row>
                        <Form.Item label="Imagens adicionais" style={{ width: "100%" }}>
                            <Upload
                                action="/api/admin/shop/upload-image"
                                listType="picture-card"
                                fileList={fileList}
                                onPreview={handlePreview}
                                onChange={handleChange}
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
                    placeholder={`Pesquisar ${dataIndex}`}
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
    ];

    const data = [
        {
            key: "1",
            item_name: "Espada de Ouro",
            category: "Armas",
            type: "Permanente",
            tag: "Épico",
            price: 500,
        },
        {
            key: "2",
            item_name: "Elmo de Ferro",
            category: "Roupas",
            type: "Permanente",
            tag: "Comum",
            price: 200,
        },
        {
            key: "3",
            item_name: "Poção de Cura",
            category: "Consumíveis",
            type: "Consumível",
            tag: "Raro",
            price: 50,
        },
    ];

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
