import React, { Component } from 'react'
import './New.css'
import api from '../services/api'
class New extends Component {
    state = {
        image: null,
        person: '',
    }

    handleSubmit = async e => {
        e.preventDefault()

        const data = new FormData()
        data.append('file', this.state.image)
        data.append('name', this.state.person)

        await api.post('/recognition/insert/pictures/person-name', data)
        .then(res => {
            alert('Pessoa salva' + this.state.person)
         }).catch(error =>{
            alert( error)
            console.log(error)
         })
    }

    handleChange = e => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    handleImageChange = e => {
        this.setState({image: e.target.files[0]})
        let reader = new FileReader();     
        reader.onloadend = () => {
        this.setState({ imagePreviewUrl: reader.result});
        } 
         reader.readAsDataURL(e.target.files[0])
    }

    render() {
        let preview 

        if (this.state.imagePreviewUrl) {
            preview = (<div align="center" ><img src={this.state.imagePreviewUrl} alt="icon" width="200" /> </div>);
        }

        return (
            <form id="new-post">
                <input type="file" onChange={(e) => this.handleImageChange(e)}/>
                {preview}
                <input type="text"  name="person" placeholder="Nome Pessoa" onChange={this.handleChange} value={this.state.person} />
                <button type="submit" onClick={this.handleSubmit}>Enviar</button>
            </form>
        )
    }
}

export default New
