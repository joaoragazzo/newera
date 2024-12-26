export function NotificationContent( {title, description} ) {
    return (
        <div>
            <div style={{ fontWeight: "bold" }}>
                {title}
            </div>
            <div>
                {description}
            </div>
        </div>
    )
}