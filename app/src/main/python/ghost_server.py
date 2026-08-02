from flask import Flask, request
import subprocess, os, threading

app = Flask(__name__)

@app.route('/')
def index():
    return open(os.path.join(os.path.dirname(__file__), '../assets/dashboard.html'), 'r', encoding='utf-8').read()

@app.route('/run')
def run():
    cmd = request.args.get('tool', 'echo no command')
    try:
        result = subprocess.check_output(cmd, shell=True, timeout=10, stderr=subprocess.STDOUT).decode()
        return result
    except Exception as e:
        return str(e)

def start_server():
    threading.Thread(target=lambda: app.run(host='127.0.0.1', port=5000, debug=False), daemon=True).start()
