const toggleSidebar = () => {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    if (window.getComputedStyle(sidebar).display !== 'none') {
        sidebar.style.display = 'none';
        content.style.marginLeft = '0%';
    } else {
        sidebar.style.display = 'block';
        content.style.marginLeft = '20%';
    }
};

const search = () => {
    const query = document.getElementById("search-input").value;
//    console.log(query)
    if (query === null || query.trim() === "") {
        document.querySelector(".search-result").style.display = "none";
    } else {
        const url = `http://localhost:8080/user/search/${query}`;

        fetch(url)
            .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.statusText}`);
            }
            return response.text(); // First, get the response as text
        })
            .then(text => {
            try {
                const data = JSON.parse(text); // Try to parse the text as JSON
                let output = `<div class='list-group'>`;
                data.forEach(contact => {
                    output += `<a href='/user/contact/${contact.cid}' class='list-group-item list-group-item-action'>${contact.cname}</a>`;
                });
                output += `</div>`;
                document.querySelector(".search-result").innerHTML = output;
                document.querySelector(".search-result").style.display = "block";
            } catch (error) {
                console.error('Error parsing JSON:', error.message);
                document.querySelector(".search-result").style.display = "none"; // Hide results if parsing fails
            }
        })
            .catch(error => {
            console.error('Error fetching data:', error.message);
            document.querySelector(".search-result").style.display = "none"; // Hide results if fetching fails
        });
    }
};
