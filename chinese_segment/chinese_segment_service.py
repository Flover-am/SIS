from flask import Flask, request, jsonify
from LAC import LAC

app = Flask(__name__)


@app.route('/v2/segment', methods=['GET'])
def text_segment():
    query = request.args.get('query', '', type=str)
    lac = LAC(mode='seg')
    words = lac.run(query)
    return jsonify({
        'code': 200,
        'msg': 'success',
        'data': words
    })


@app.route('/v2/lac', methods=['GET'])
def text_lac():
    query = request.args.get('query', '', type=str)
    lac = LAC(mode='lac')
    words = lac.run(query)
    return jsonify({
        'code': 200,
        'msg': 'success',
        'data': {
            'words': words[0],
            'tags': words[1]
        }
    })


@app.route('/v2/rank', methods=['GET'])
def text_rank():
    query = request.args.get('query', '', type=str)
    lac = LAC(mode='rank')
    words = lac.run(query)
    return jsonify({
        'code': 200,
        'msg': 'success',
        'data': words
    })


if __name__ == '__main__':
    app.run(debug=True)
