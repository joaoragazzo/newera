import { useState, useEffect } from "react";

import { Cascader, Col, Form, Input, InputNumber, Modal, Row, Select, Upload, Image, Card, Divider, message } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import axios from "axios";
import StockNumberNameWithTooltip from "./tooltips/StockNumberNameWithTooltip";
import YoutubeNameWithTooltip from "./tooltips/YoutubeNameWithTooltip";
import TagsNameWithTooltip from "./tooltips/TagsNameWithTooltop";
import PriceNameWithTooltip from "./tooltips/PriceNameWithTooltip";
import ThumbnailUpload from "./AddItemModalComponents/ThumbnailUpload";
import YoutubeLinks from "./AddItemModalComponents/YoutubeLinks";
import NotificationSettings from "./AddItemModalComponents/NotificationSettings";
import InterationsSettings from "./AddItemModalComponents/InteractionsSettings";
import DeliverySettings from "./AddItemModalComponents/DeliverySettings";

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

const uploadButton = (
    <button style={{ border: 0, background: 'none', color: 'white', cursor: "pointer" }} type="button">
        <PlusOutlined />
        <div style={{ marginTop: 8, color: 'white', cursor: "pointer" }}>Upload</div>
    </button>
);

const AddItemModal = ( { isAddModalVisible, setIsAddModalVisible } ) => {
    
    const [previewImage, setPreviewImage] = useState('');
    const [previewOpen, setPreviewOpen] = useState(false);
    const [fileList, setFileList] = useState([]);
    const [category, setCategories] = useState([]);
    const [tags, setTags] = useState([]);
    const [addItemForm] = Form.useForm();
    

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
      
    const handlePreview = async (file) => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj);
        }

        setPreviewImage(file.url || (file.preview));
        setPreviewOpen(true);
    };

    const getBase64 = (file) =>
            new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = (error) => reject(error);
            });


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
                            <Form.Item label={< PriceNameWithTooltip />} name="price" style={{ width: "100%" }} rules={[
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
                            <ThumbnailUpload addItemForm={addItemForm}/>
                        </Form.Item>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={14}>
                        <Form.Item label={< TagsNameWithTooltip />} name="tags" >
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
                        <YoutubeLinks />
                    </Form.Item>
                </Row>
                <Row gutter={[16, 16]}>
                    <Col span={24}>
                        <Card title="Notificações">
                            <NotificationSettings />
                        </Card>
                    </Col>
                    <Col span={24}>
                        <Card title="Interações">
                            <InterationsSettings />
                        </Card>
                    </Col>
                    <Col span={24}>
                        <Card title="Entrega">
                            <DeliverySettings />
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

export default AddItemModal;