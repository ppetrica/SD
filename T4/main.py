import pymongo
from bson import ObjectId

from flask import Flask, request, jsonify


def main():
    client = pymongo.MongoClient()
    db = client['SD']
    spending_db = db['spending']

    app = Flask('Cheltuieli')

    @app.route('/')
    def index():
        return 'Hello'

    @app.route('/spending', methods=['GET', 'POST', 'DELETE', 'PUT'])
    def spending():
        if request.method == 'GET':
            return spending_db.find(request.json)
        elif request.method == 'POST':
            return handle_post()
        elif request.method == 'DELETE':
            return handle_delete()
        elif request.method == 'PUT':
            return handle_put()

    def handle_post():
        if not all(k in request.json for k in ['name', 'price', 'buyer', 'category']):
            return "Invalid request.", 500

        insert_res = spending_db.insert_one(request.json)

        return jsonify(result="OK", _id=insert_res.inserted_id), 200

    def handle_delete():
        if '_id' not in request.json:
            return "Invalid request", 500

        delete_res = spending_db.delete_one({'_id': request.json['_id']})

        if delete_res.deleted_count == 1:
            return jsonify(result="OK"), 200
        else:
            return jsonify(result="Failed"), 404

    def handle_put():
        updated = request.json

        if '_id' not in updated:
            return "Invalid request", 500

        _id = ObjectId(updated['_id'])

        del updated['_id']

        update_res = spending_db.update_one({'_id': ObjectId(request.json['_id'])}, {'$set': updated})

        if update_res.modified_count == 1:
            return jsonify(result="OK"), 200
        else:
            return jsonify(result="Failed"), 404

    app.run()


if __name__ == '__main__':
    main()
