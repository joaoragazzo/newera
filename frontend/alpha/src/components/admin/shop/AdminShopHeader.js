import { Button, Divider } from "antd";
import { FaBox, FaList, FaPercentage } from "react-icons/fa";
import { MdAutoGraph, MdDiscount, MdFolderCopy } from "react-icons/md";


const buttonStyle = {
    width: '100%'
}

const AdminShopHeader = ({ setActivePage, activePage }) => {
    return (<><Button
        style={buttonStyle}
        icon={<FaBox />}
        type={activePage === 'itens' ? 'primary' : 'default'}
        onClick={() => setActivePage('itens')}
    >
        Itens
    </Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<MdFolderCopy />}
            type={activePage === 'category' ? 'primary' : 'default'}
            onClick={() => setActivePage('category')}
        >
            Categorias
        </Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<MdFolderCopy />}
            type={activePage === 'tags' ? 'primary' : 'default'}
            onClick={() => setActivePage('tags')}
        >
            Tags
        </Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<MdDiscount />}
            type={activePage === 'cupons' ? 'primary' : 'default'}
            onClick={() => setActivePage('cupons')}
        >
            Cupons
        </Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<FaPercentage />}
            type={activePage === 'sales' ? 'primary' : 'default'}
            onClick={() => setActivePage('sales')}
        >
            Promoções
        </Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<FaList />}
            type={activePage === 'status' ? 'primary' : 'default'}
            onClick={() => setActivePage('status')}
        >
            Status
            de Benefícios</Button>
        <Divider type="vertical" />
        <Button
            style={buttonStyle}
            icon={<MdAutoGraph />}
            type={activePage === 'report' ? 'primary' : 'default'}
            onClick={() => setActivePage('report')}
        >
            Relatório de vendas
        </Button></>);
}

export default AdminShopHeader;