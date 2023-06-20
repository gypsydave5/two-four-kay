function copyToClipboard(text) {
    if ('clipboard' in window.navigator) {
        return navigator.clipboard.writeText(text)
            .then(() => 'Copied')
    }
    return Promise.reject('no clipboard')
}