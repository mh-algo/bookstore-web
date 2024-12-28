function escapeHTML(str) {
    if (typeof str !== 'string') return str; // 문자열이 아닌 경우 반환
    return str.replace(/[&<>"']/g, match => {
        const escapeMap = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'
        };
        return escapeMap[match];
    });
}