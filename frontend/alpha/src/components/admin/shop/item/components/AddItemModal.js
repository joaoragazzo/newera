import { Cascader, Col, Form, Input, InputNumber, Modal, Row, Select, Upload } from "antd";


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

const AddModal = ( { isAddModalVisible, onOkAddFile, addItemForm, onFinishAddForm } ) => {
    
    const [thumbnail, setThumbnail] = useState();

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
