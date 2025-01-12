import { useState } from "react";
import { Button, Card, Col, Divider, Row, Layout, Flex, Typography } from "antd";
import { MdDiscount, MdFolderCopy, MdAutoGraph } from "react-icons/md";
import { FaPercentage, FaBox, FaList } from "react-icons/fa";
import ItemManager from "../../components/admin/shop/item/ItemManager";
import AdminShopHeader from "../../components/admin/shop/AdminShopHeader";
import CategoryManager from "../../components/admin/shop/item/CategoryManager";
import TagManager from "../../components/admin/shop/item/TagManager";

const { Header, Content } = Layout;
const { Title } = Typography;



const headerStyle = {
    flex: 1,
    marginBottom: '25px',
    height: '50px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
}

const AdminShop = () => {
    const [activePage, setActivePage] = useState('itens');

    const renderContent = () => {
        switch (activePage) {
            case 'itens':
                return <ItemManager />;
            case 'category':
                return <CategoryManager />;
            case 'tags':
                return <TagManager />
            case 'cupons':
                return <div>Gestão de Cupons</div>;
            case 'sales':
                return <div>Gestão de Promoções</div>;
            case 'status':
                return <div>Status dos Benefícios Temporários</div>;
            case 'report':
                return <div>Relatório de Vendas</div>;
        }
    };

    return (
        <>
            <Card>
                <Title style={{textAlign: "center", marginBottom: "50px"}}>CONFIGURAÇÕES DA LOJA</Title>
                <Header style={headerStyle}>
                    <AdminShopHeader setActivePage={setActivePage} activePage={activePage} />
                </Header>
                <Content style={{ marginTop: "20px", marginLeft: "50px", marginRight: "50px" }}>
                    {renderContent()}
                </Content>
            </Card>
        </>
    );
}

export default AdminShop;