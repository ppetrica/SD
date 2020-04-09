import os
import sys
from PyQt5.QtWidgets import QWidget, QApplication, QFileDialog, QMessageBox, QDialog, QLineEdit
from PyQt5 import QtCore
from PyQt5.uic import loadUi
from mq_communication import RabbitMq


def debug_trace(ui=None):
    from pdb import set_trace
    QtCore.pyqtRemoveInputHook()
    set_trace()
    # QtCore.pyqtRestoreInputHook()


ROOT_DIR = os.path.dirname(os.path.abspath(__file__))


class BookDialog(QDialog):
    def __init__(self):
        QDialog.__init__(self)
        loadUi(os.path.join(ROOT_DIR, 'popup.ui'), self)

    def get_entries(self):
        return self.author_input.text(),\
                self.text_input.text(),\
                self.name_input.text(),\
                self.publisher_input.text()


class LibraryApp(QWidget):
    def __init__(self):
        super(LibraryApp, self).__init__()
        ui_path = os.path.join(ROOT_DIR, 'exemplul_2.ui')
        loadUi(ui_path, self)
        self.add_btn.clicked.connect(self.add)
        self.search_btn.clicked.connect(self.search)
        self.save_as_file_btn.clicked.connect(self.save_as_file)
        self.rabbit_mq = RabbitMq(self)
        self._dialog = BookDialog()
        self._dialog.accepted.connect(self.add_new_book)

    def set_response(self, response):
        self.result.setText(response)

    def send_request(self, request):
        self.rabbit_mq.send_message(message=request)
        self.rabbit_mq.receive_message()

    def search(self):
        search_string = self.search_bar.text()
        if not search_string:
            if self.json_rb.isChecked():
                request = 'print:json'
            elif self.html_rb.isChecked():
                request = 'print:html'
            elif self.xml_rb.isChecked():
                request = 'print:xml'
            else:
                request = 'print:raw'
        else:
            if self.author_rb.isChecked():
                request = 'find:author={}'.format(search_string)
            elif self.title_rb.isChecked():
                request = 'find:title={}'.format(search_string)
            else:
                request = 'find:publisher={}'.format(search_string)
        self.send_request(request)

    def save_as_file(self):
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        file_path = str(
            QFileDialog.getSaveFileName(self,
                                        'Salvare fisier',
                                        options=options))
        if file_path:
            file_path = file_path.split("'")[1]
            _, ext = os.path.splitext(file_path)
            if not ext == '.json'\
                and not ext == '.html' \
                    and not ext == '.xml' \
                    and not ext == '.txt':
                if self.json_rb.isChecked():
                    file_path += '.json'
                elif self.html_rb.isChecked():
                    file_path += '.html'
                elif self.xml_rb.isChecked():
                    file_path += '.xml'
                else:
                    file_path += '.txt'
            try:
                # Sa modific aici partea asta ar insemna sa stiu de dinainte format-ul pe care il primesc de la server,
                # sau sa fac din nou o cerere cu formatul dorit, nici una din variante nu mi se pare ok
                with open(file_path, 'w') as fp:
                    if ext == '.html':
                        fp.write(self.result.toHtml())
                    else:
                        fp.write(self.result.toPlainText())
            except Exception as e:
                print(e)
                QMessageBox.warning(self, 'Exemplul 2',
                                    'Nu s-a putut salva fisierul')

    def add(self):
        self._dialog.show()

    def add_new_book(self):
        author, text, name, publisher = self._dialog.get_entries()

        self.send_request(f'add:{author}&{text}&{name}&{publisher}')


if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = LibraryApp()
    window.show()
    sys.exit(app.exec_())
